/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.linkedin.camus.example.records;  
@SuppressWarnings("all")
/** Logs for really important stuff. */
public class DummyLog2 extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DummyLog2\",\"namespace\":\"com.linkedin.camus.example.records\",\"doc\":\"Logs for really important stuff.\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"muchoStuff\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}");
  @Deprecated public long id;
  @Deprecated public java.lang.CharSequence name;
  @Deprecated public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> muchoStuff;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return name;
    case 2: return muchoStuff;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: name = (java.lang.CharSequence)value$; break;
    case 2: muchoStuff = (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.Long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'muchoStuff' field.
   */
  public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> getMuchoStuff() {
    return muchoStuff;
  }

  /**
   * Sets the value of the 'muchoStuff' field.
   * @param value the value to set.
   */
  public void setMuchoStuff(java.util.Map<java.lang.CharSequence,java.lang.CharSequence> value) {
    this.muchoStuff = value;
  }

  /** Creates a new DummyLog2 RecordBuilder */
  public static com.linkedin.camus.example.records.DummyLog2.Builder newBuilder() {
    return new com.linkedin.camus.example.records.DummyLog2.Builder();
  }
  
  /** Creates a new DummyLog2 RecordBuilder by copying an existing Builder */
  public static com.linkedin.camus.example.records.DummyLog2.Builder newBuilder(com.linkedin.camus.example.records.DummyLog2.Builder other) {
    return new com.linkedin.camus.example.records.DummyLog2.Builder(other);
  }
  
  /** Creates a new DummyLog2 RecordBuilder by copying an existing DummyLog2 instance */
  public static com.linkedin.camus.example.records.DummyLog2.Builder newBuilder(com.linkedin.camus.example.records.DummyLog2 other) {
    return new com.linkedin.camus.example.records.DummyLog2.Builder(other);
  }
  
  /**
   * RecordBuilder for DummyLog2 instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DummyLog2>
    implements org.apache.avro.data.RecordBuilder<DummyLog2> {

    private long id;
    private java.lang.CharSequence name;
    private java.util.Map<java.lang.CharSequence,java.lang.CharSequence> muchoStuff;

    /** Creates a new Builder */
    private Builder() {
      super(com.linkedin.camus.example.records.DummyLog2.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.linkedin.camus.example.records.DummyLog2.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing DummyLog2 instance */
    private Builder(com.linkedin.camus.example.records.DummyLog2 other) {
            super(com.linkedin.camus.example.records.DummyLog2.SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = (java.lang.Long) data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = (java.lang.CharSequence) data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.muchoStuff)) {
        this.muchoStuff = (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>) data().deepCopy(fields()[2].schema(), other.muchoStuff);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'id' field */
    public java.lang.Long getId() {
      return id;
    }
    
    /** Sets the value of the 'id' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'id' field has been set */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'id' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'name' field */
    public java.lang.CharSequence getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder setName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'name' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'muchoStuff' field */
    public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> getMuchoStuff() {
      return muchoStuff;
    }
    
    /** Sets the value of the 'muchoStuff' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder setMuchoStuff(java.util.Map<java.lang.CharSequence,java.lang.CharSequence> value) {
      validate(fields()[2], value);
      this.muchoStuff = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'muchoStuff' field has been set */
    public boolean hasMuchoStuff() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'muchoStuff' field */
    public com.linkedin.camus.example.records.DummyLog2.Builder clearMuchoStuff() {
      muchoStuff = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public DummyLog2 build() {
      try {
        DummyLog2 record = new DummyLog2();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.muchoStuff = fieldSetFlags()[2] ? this.muchoStuff : (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
