package net.explorviz.extension.heatmap.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.github.jasminb.jsonapi.annotations.Id;
import java.util.concurrent.atomic.AtomicLong;

// Needed for cyclical serialization
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "id")
public class BaseEntity {

  private static final AtomicLong ID_GENERATOR = new AtomicLong();

  @Id
  private String id;

  public BaseEntity() {
    this.id = String.valueOf(ID_GENERATOR.incrementAndGet());
  }

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void updateId() {
    this.id = String.valueOf(ID_GENERATOR.incrementAndGet());
  }

}
