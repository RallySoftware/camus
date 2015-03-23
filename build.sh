#!/bin/bash
set -e

VERSION=0.1.2
if [ -z "$BUILD_NUMBER" ]; then
	SNAPSHOT="-SNAPSHOT"	
fi

while [ "$#" -gt 0 ]; do
  case "$1" in 
  -f) 
     OVERWRITE_POM=1
     shift
     ;;
  *) 
     echo "Unknown option $1"
     exit 1
     ;;
  esac
done


# Must add the following to repo to the default profile in .m2/settings.xml
# <repositories>
#    <repository>
#         		<id>Cloudera</id>
#         		<name>Cloudera repository</name>
#         		<url>https://repository.cloudera.com/content/repositories/releases</url>
#         		<releases>
#         			<enabled>true</enabled>
#         		</releases>
#         		<snapshots>
#         			<enabled>false</enabled>
#         		</snapshots>
#         	</repository>
#  </repositories> 

function set-version() {
	local VERSION=$1
	xmlstarlet edit -L -N x="http://maven.apache.org/POM/4.0.0" -u "//x:project/x:version" -v "$VERSION"  pom.xml
	xmlstarlet edit -L -N x="http://maven.apache.org/POM/4.0.0" -u "//x:project/x:properties/x:project.version" -v "$VERSION"  pom.xml
	find . -name "pom.xml" | xargs -P1 xmlstarlet edit -L -N x="http://maven.apache.org/POM/4.0.0" -u "//x:project/x:parent/x:version" -v "$VERSION" 
}

which xmlstarlet > /dev/null || {
  echo "Please install xmlstarlet" 
  exit 2
}

if git status | grep pom.xml > /dev/null; then 
  if [ -z "$OVERWRITE_POM" ]; then 
    echo "This script modifies pom.xml files.  Please commit, stash, or revert any changes"
    exit 3;
  fi
  find . -name "pom.xml" | xargs git checkout 
fi

# Clean
rm -rf mvn-repo

# Deploy required dependency not in maven central public repo
mvn deploy:deploy-file -DgroupId=org.apache.avro -DartifactId=avro-repo-bundle -Dversion=1.7.4-SNAPSHOT -Dpackaging=jar -Dfile=lib/avro-repo-bundle-1.7.4-SNAPSHOT-withdeps.jar   -Durl=file://$(pwd)/target/mvn-repo

# --- Build Hadoop 1 ---
# update pom files
set-version $VERSION-hadoop1$SNAPHOT
mvn -DaltDeploymentRepository=internal.repo::default::file://$(pwd)/target/mvn-repo clean deploy
find . -name "pom.xml" | xargs git checkout 

# --- Build Cloudera ---
# update pom files
set-version $VERSION-cdh4$SNAPSHOT
mvn -Dhadoop-client.version=2.0.0-mr1-cdh4.2.0 -DaltDeploymentRepository=internal.repo::default::file://$(pwd)/target/mvn-repo clean deploy
find . -name "pom.xml" | xargs git checkout 

