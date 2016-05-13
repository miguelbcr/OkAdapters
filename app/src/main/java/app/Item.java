package app;

public class Item {
    private final int id;

    public Item(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override public String toString() {
        return "id : " + id + " Created at -> " + System.currentTimeMillis();
    }
}
