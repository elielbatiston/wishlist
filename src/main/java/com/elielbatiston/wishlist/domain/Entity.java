package com.elielbatiston.wishlist.domain;

public abstract class Entity {
  private String id;

  public Entity(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
