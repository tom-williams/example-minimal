package org.example.domain;

import io.ebean.DB;
import io.ebean.Database;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * When running tests in the IDE install the "Enhancement plugin".
 * <p>
 * http://ebean-orm.github.io/docs/setup/enhancement#ide
 */
class CustomerTest {


  /**
   * Get the "default database" and save().
   */
  @Test
  void insert_via_database() {

    Customer rob = new Customer("Rob");

    Database server = DB.getDefault();
    server.save(rob);

    assertThat(rob.getId()).isNotNull();
  }

  /**
   * Use the Ebean singleton (effectively using the "default server").
   */
  @Test
  void insert_via_model() {

    Customer jim = new Customer("Jim");
    jim.save();

    assertThat(jim.getId()).isNotNull();
  }


  /**
   * Find and then update.
   */
  @Test
  void updateRob() {

    Customer newBob = new Customer("Bob");
    newBob.save();

    Customer bob = DB.find(Customer.class)
      .where().eq("name", "Bob")
      .findOne();

    bob.setNotes("Doing an update");
    bob.save();
  }

  /**
   * Execute an update without a prior query.
   */
  @Test
  void statelessUpdate() {

    Customer newMob = new Customer("Mob");
    newMob.save();

    Customer upd = new Customer();
    upd.setId(newMob.getId());
    upd.setNotes("Update without a fetch");

    upd.update();
  }

  @Test
  void duplicateKeyTransitiveStatelessUpdate() {
    Customer tom = new Customer("Tom");
    Order order1 = new Order();

    tom.addOrder(order1);

    tom.save();

    UUID order1Id = order1.getId();

    // Simulate a Customer object having been deserialized by the API,
    // and calling the setOrders for a Customer object that has already been persisted, but needs to be updated
    // Customer object:
    // { "name": "Tom", "id": "8986e2b8-abd5-41ad-93a6-9e48d486b132", "orders": ["id": "4b14b111-a0ce-4d1e-badd-1de9ae7889f3"] }
    Order order1NotLoaded = new Order();
    order1NotLoaded.setId(order1Id);
    tom.setOrders(List.of(order1NotLoaded));

    tom.update();
  }
}
