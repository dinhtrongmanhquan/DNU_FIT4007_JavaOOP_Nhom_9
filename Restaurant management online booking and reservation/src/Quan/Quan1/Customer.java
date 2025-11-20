package Quan.Quan1;

public class Customer extends Person {
    private String phone;
    private int age;

    public Customer(String id, String name, String phone, int age) {
        super(id, name);
        this.phone = phone;
        this.age = age;
    }

    public String getPhone() {
        return this.phone;
    }

    public int getAge() {
        return this.age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }
}