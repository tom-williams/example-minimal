package org.example.domain;

import io.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Customer extends Model {

  @Id
  UUID id;

  String name;

  String notes;

  @Version
  long version;

  public Customer(String name) {
    this.name = name;
  }

  /**
   * Only used to demonstrate "stateless update".
   */
  public Customer() {

  }

  public UUID getId() {
    return id;
  }

  @OneToMany(cascade = CascadeType.ALL)
  public List<Order> orders;

  public List<Order> getOrders() {
    if (orders == null) {
      orders = new ArrayList<>();
    }
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public void addOrder(Order order) {
    order.setCustomer(this);
    getOrders().add(order);
  }

  public void removeOrder(Order order) {
    order.setCustomer(null);
    getOrders().remove(order);
  }
  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

}
