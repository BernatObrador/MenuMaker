package com.example.firebaseprueba;

public interface onQuantityChangeListener {
    void onQuantityIncreased(String category, int newQuantity);
    void onQuantityDecreased(String category, int newQuantity);
}
