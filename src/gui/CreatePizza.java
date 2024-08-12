package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

// Flyweight Pattern
class IngredientFactory {

    private IngredientFactory() {
    }

    private static final HashMap<String, IngredientFactory> ingredientsPOOL = new HashMap<>();

    public static IngredientFactory createIngredient(String ingredient) {

        IngredientFactory instance = ingredientsPOOL.get(ingredient);

        if (instance == null) {
            instance = new IngredientFactory();
            instance.ingredient = ingredient;
            ingredientsPOOL.put(ingredient, instance);
        }
        return instance;
    }
    private String ingredient;

    public String getIngredient() {
        return ingredient;
    }

    public IngredientFactory setIngredient(String ingredient) {
        return IngredientFactory.createIngredient(ingredient);
    }

}

// Interpreter Pattern
interface Command {

    void execute(Pizza pizza);
}

class DefaultPizzaCommand implements Command {

    private String topping;

    public DefaultPizzaCommand(String topping) {
        this.topping = topping;
    }

    @Override
    public void execute(Pizza pizza) {

        pizza = new Pizza.Builder()
                .setType(pizza.type)
                .setSize(pizza.size)
                .setBasePrice(pizza.basePrice)
                .addAllToppings(pizza.toppings) 
                .build();
        pizza.display(); 
    }
}

class CustomizePizzaCommand implements Command {

    private String type;
    private String size;
    private List<String> toppings;

    public CustomizePizzaCommand(String type, String size, List<String> toppings) {
        this.type = type;
        this.size = size;
        this.toppings = toppings;
    }

    @Override
    public void execute(Pizza pizza) {

        pizza = new Pizza.Builder()
                .setType(type)
                .setSize(size)
                .setBasePrice(pizza.basePrice) 
                .addAllToppings(toppings) 
                .build();
        pizza.display(); 
    }
}

// Mediator Pattern
class OrderMediator {

    private final Manager manager;
    private final Customer customer;

    public OrderMediator(Manager manager, Customer customer) {
        this.manager = manager;
        this.customer = customer;
    }

    public void findOrder() {
        JOptionPane.showMessageDialog(null, "Order Manager: Find Order", "Information", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Order Manager: Find Order");
        manager.orderConfirmation();
    }

}

//component
abstract class User {

    protected OrderMediator orderMediator;

    public final void setOrderMediator(OrderMediator taxiCenterApp) {
        this.orderMediator = taxiCenterApp;
    }
}

class Manager extends User {

    public  static  boolean orderConfirm = false;
    
    public void orderConfirmation() {
        
        orderConfirm = true;
        JOptionPane.showMessageDialog(null, "Manager : Order Confirmation", "Information", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Manager : Order Confirmation");
    }
}

class Customer extends User {

    public void searchOrder() {
        JOptionPane.showMessageDialog(null, HomePanel.userName + " : Pizza Orded", "Information", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(HomePanel.userName + " : Pizza Orded");
        orderMediator.findOrder();
    }
}

// Chain of Responsibility Pattern
abstract class OrderStep {

    protected OrderStep nextStep;

    public void setNextStep(OrderStep nextStep) {
        this.nextStep = nextStep;
    }

    public abstract void processStep();
}

class AcceptingStep extends OrderStep {

    @Override
    public void processStep() {
        JOptionPane.showMessageDialog(null, "Order is accepted.", "Information", JOptionPane.INFORMATION_MESSAGE);
        if (nextStep != null) {
            nextStep.processStep();
        }
    }
}

class CookingStep extends OrderStep {

    @Override
    public void processStep() {
        JOptionPane.showMessageDialog(null, "Pizza is being cooked.", "Information", JOptionPane.INFORMATION_MESSAGE);
        if (nextStep != null) {
            nextStep.processStep();
        }
    }
}

class PackingStep extends OrderStep {

    @Override
    public void processStep() {
        JOptionPane.showMessageDialog(null, "Pizza is being packed.", "Information", JOptionPane.INFORMATION_MESSAGE);
        if (nextStep != null) {
            nextStep.processStep();
        }
    }
}

class HandoverStep extends OrderStep {

    @Override
    public void processStep() {
        JOptionPane.showMessageDialog(null, "Pizza is handed over to the driver for delivery.", "Information", JOptionPane.INFORMATION_MESSAGE);
        if (nextStep != null) {
            nextStep.processStep();
        }
    }
}

// Build Pattern
class Pizza {

    String type;
    String size;
    double basePrice;
    List<String> toppings;
    public static double pizzaTotalPrice;

    private Pizza(String type, String size, double basePrice, List<String> toppings) {
        this.type = type;
        this.size = size;
        this.basePrice = basePrice;
        this.toppings = toppings;
    }

    public static class Builder {

        private String type;
        private String size;
        private double basePrice;
        private List<String> toppings = new ArrayList<>();

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder setBasePrice(double basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public Builder addAllToppings(List<String> toppings) {
            this.toppings.addAll(toppings);
            return this;
        }

        public Pizza build() {
            return new Pizza(type, size, basePrice, toppings);
        }
    }

    public void display() {

        double toppingsPrice = toppings.size() * 150;
        pizzaTotalPrice = basePrice + toppingsPrice;

        System.out.println("Pizza: " + type + ", Size: " + size + ", Toppings: " + String.join(", ", toppings) + " \nTotal Price : Rs " + pizzaTotalPrice);

    }

}
