package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBeanWithDependencyImpl implements MyBeanWithDependency{

    private MyOperation myOperation;

    public MyBeanWithDependencyImpl(MyOperation myOperation) {
        this.myOperation = myOperation;
    }

    @Override
    public void printWithDependency() {
        int num = 1;
        System.out.println(myOperation.sum(num));
        System.out.println("Hola desde la implementación de un bean con dependencia");
    }
}
