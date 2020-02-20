package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Id;

// Needed for cyclical serialization
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class BaseEntity {


  // private static final AtomicLong ID_GENERATOR = new AtomicLong();

  @Id
  @JsonProperty("id")
  private String id;

  public BaseEntity(final String id) {
    // this.id = String.valueOf(ID_GENERATOR.incrementAndGet());
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  @JsonSetter
  public void setId(final String id) {
    this.id = id;
  }
}
