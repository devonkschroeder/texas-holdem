package hannahschroeder.texasholdem;

class Pot {
    int potTotal;

    public Pot(int startAmount) {
        potTotal = startAmount;
    }

    public int getTotal() {
        return potTotal;
    }

    public void addValue(int value) {
        potTotal += value;
    }

    public void removeValue(int value) {
        potTotal -= value;
    }
}