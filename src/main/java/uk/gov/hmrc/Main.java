package uk.gov.hmrc;

class Vehicle { }
class Car extends Vehicle { }
class SportsCar extends Car { }
class Motorcycle extends Vehicle { }

class Garage {
  private List<? super Car> carArea; 

  public Garage() {
    this.carArea = new ArrayList<>(); // Initialize the car area
  }

  public void parkCar(Car car) {
    this.carArea.add(car); 
  }

  // ... other methods ...
}

public class Main {
  public static void main(String[] args) {
    Garage garage = new Garage();
    garage.parkCar(new Car());       // OK: Adding a Car
    garage.parkCar(new SportsCar()); // OK: Adding a subtype of Car
    // garage.parkCar(new Motorcycle()); // Error: Cannot add a Motorcycle

    List<? super Car> vehicleList = new ArrayList<Vehicle>(); // Assigning a more general type
    vehicleList.add(new Car()); // Adding a Car
    vehicleList.add(new SportsCar()); // Adding a SportsCar
    // vehicleList.add(new Motorcycle()); // Error: Cannot add a Motorcycle
  }
}