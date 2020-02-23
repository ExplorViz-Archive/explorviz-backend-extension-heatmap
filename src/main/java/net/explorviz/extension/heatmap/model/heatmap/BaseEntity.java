package net.explorviz.extension.heatmap.model.heatmap;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Id;
import net.explorviz.shared.common.idgen.IdGenerator;

// Needed for cyclical serialization
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class BaseEntity {

  @Id
  @JsonProperty("id")
  private String id;

  public BaseEntity(final String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  @JsonSetter
  public void setId(final String id) {
    this.id = id;
  }

  public void updateId(final IdGenerator idgen) {
    this.id = idgen.generateId();
  }
}
