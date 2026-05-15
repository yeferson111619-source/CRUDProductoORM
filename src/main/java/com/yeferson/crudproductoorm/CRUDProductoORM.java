package com.yeferson.crudproductoorm;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Scanner;

public class CRUDProductoORM {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n===== CRUD PRODUCTOS CON ORM =====");
            System.out.println("1. Ingresar producto");
            System.out.println("2. Leer productos");
            System.out.println("3. Actualizar producto");
            System.out.println("4. Borrar producto");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> ingresarProducto();
                case 2 -> leerProductos();
                case 3 -> actualizarProducto();
                case 4 -> borrarProducto();
                case 5 -> System.out.println("Programa finalizado.");
                default -> System.out.println("Opción incorrecta.");
            }

        } while (opcion != 5);

        HibernateUtil.getSessionFactory().close();
    }

    public static void ingresarProducto() {
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        Producto producto = new Producto(nombre, descripcion, precio, stock);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(producto);

        transaction.commit();
        session.close();

        System.out.println("Producto ingresado correctamente.");
    }

    public static void leerProductos() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Producto> productos = session
                .createQuery("FROM Producto", Producto.class)
                .list();

        System.out.println("\nListado de productos:");

        for (Producto producto : productos) {
            System.out.println(producto);
        }

        session.close();
    }

    public static void actualizarProducto() {
        System.out.print("Ingrese el ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Producto producto = session.get(Producto.class, id);

        if (producto != null) {
            System.out.print("Nuevo nombre: ");
            producto.setNombre(scanner.nextLine());

            System.out.print("Nueva descripción: ");
            producto.setDescripcion(scanner.nextLine());

            System.out.print("Nuevo precio: ");
            producto.setPrecio(scanner.nextDouble());

            System.out.print("Nuevo stock: ");
            producto.setStock(scanner.nextInt());

            session.merge(producto);
            transaction.commit();

            System.out.println("Producto actualizado correctamente.");
        } else {
            System.out.println("Producto no encontrado.");
            transaction.rollback();
        }

        session.close();
    }

    public static void borrarProducto() {
        System.out.print("Ingrese el ID del producto a borrar: ");
        int id = scanner.nextInt();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Producto producto = session.get(Producto.class, id);

        if (producto != null) {
            session.remove(producto);
            transaction.commit();

            System.out.println("Producto eliminado correctamente.");
        } else {
            System.out.println("Producto no encontrado.");
            transaction.rollback();
        }

        session.close();
    }
}