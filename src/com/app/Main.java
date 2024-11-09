package com.app;

import java.text.NumberFormat;
import java.util.*;

class Menu {
    String nama, kategori;
    int harga;

    public Menu(String nama, int harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getName() { return nama; }
    public int getPrice() { return harga; }
    public String getCategory() { return kategori; }
}


public class Main {

    // Method Konversi Ke Rupiah
    private static String formatIDR(int amount) {
        NumberFormat formatIDR = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatIDR.format(amount);
    }

    // Metode untuk mengubah huruf pertama dari setiap kata menjadi kapital
    private static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String[] words = text.split(" ");
        StringBuilder capitalizedText = new StringBuilder();
        for (String word : words) {
            if (word.length() > 1) {
                capitalizedText.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            } else {
                capitalizedText.append(word.toUpperCase());
            }
            capitalizedText.append(" ");
        }
        return capitalizedText.toString().trim();
    }

    // Menjadikan metode formatDataArray statik
    private static String formatDataArray(List<String> dataList) {
        Set<String> uniqueData = new HashSet<>(dataList);
        StringBuilder formattedData = new StringBuilder();

        int nomor = 1;
        for (String data : uniqueData) {
            formattedData.append(nomor).append(". ").append(data).append("\n");
            nomor++;
        }

        return formattedData.toString();
    }

    // Daftar menu
    private static final Map<String, Menu> menuMap = new HashMap<>();
    static {
        menuMap.put("nasi goreng", new Menu("Nasi Goreng", 15000, "makanan"));
        menuMap.put("mie goreng", new Menu("Mie Goreng", 15000, "makanan"));
        menuMap.put("mie rebus", new Menu("Mie Rebus", 15000, "makanan"));
        menuMap.put("kwetiau goreng", new Menu("Kwetiau Goreng", 15000, "makanan"));
        menuMap.put("kwetiau rebus", new Menu("Kwetiau Rebus", 15000, "makanan"));
        menuMap.put("es teh", new Menu("Es Teh", 5000, "minuman"));
        menuMap.put("es jeruk", new Menu("Es Jeruk", 8000, "minuman"));
        menuMap.put("jus alpukat", new Menu("Jus Alpukat", 10000, "minuman"));
        menuMap.put("jus mangga", new Menu("Jus Mangga", 10000, "minuman"));
        menuMap.put("air mineral", new Menu("Air Mineral", 3000, "minuman"));
    }

    // Menampilkan menu makanan dan minuman
    public static void displayMenu() {
        int nomor = 1;
        System.out.println("===============================");
        System.out.println("       Menu Makanan           ");
        System.out.println("===============================");
        System.out.println(nomor++ +". "+"Nasi Goreng - " + formatIDR(menuMap.get("nasi goreng").getPrice()));
        System.out.println(nomor++ +". "+"Mie Goreng - " + formatIDR(menuMap.get("mie goreng").getPrice()));
        System.out.println(nomor++ +". "+"Mie Rebus - " + formatIDR(menuMap.get("mie rebus").getPrice()));
        System.out.println(nomor++ +". "+"Kwetiau Goreng - " + formatIDR(menuMap.get("kwetiau goreng").getPrice()));
        System.out.println(nomor++ +". "+"Kwetiau Rebus - " + formatIDR(menuMap.get("kwetiau rebus").getPrice()));
        System.out.println("===============================");

        nomor = 1;
        System.out.println("       Menu Minuman           ");
        System.out.println("===============================");
        System.out.println(nomor++ +". "+"Es Teh - " + formatIDR(menuMap.get("es teh").getPrice()));
        System.out.println(nomor++ +". "+"Es Jeruk - " + formatIDR(menuMap.get("es jeruk").getPrice()));
        System.out.println(nomor++ +". "+"Jus Alpukat - " + formatIDR(menuMap.get("jus alpukat").getPrice()));
        System.out.println(nomor++ +". "+"Jus Mangga - " + formatIDR(menuMap.get("jus mangga").getPrice()));
        System.out.println(nomor++ +". "+"Air Mineral - " + formatIDR(menuMap.get("air mineral").getPrice()));
        System.out.println("===============================");
    }

    //Pesan Menu
    public static void handleOrder(Scanner scanner) {
        System.out.println("Masukkan Pesanan Anda (contoh: Nasi Goreng = 2). Ketik 'selesai' untuk menyelesaikan pesanan:");
        List<String> orderItems = new ArrayList<>();
        List<Integer> orderQuantities = new ArrayList<>();
        List<String> minumanDipesan = new ArrayList<>();
        int totalCost = 0;
        int maxOrder = 4;

        while (true) {
            System.out.print("Masukkan Item Pesanan: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("selesai")) {
                if (orderItems.isEmpty()) {
                    System.out.println("Anda Belum Memesan Pesanan");
                    continue;
                }
                break;
            }

            if (orderItems.size() >= maxOrder) {
                System.out.println("Anda sudah memesan maksimal " + maxOrder + " item. Pesanan telah ditutup.");
                break;
            }

            String[] parts = input.split(" = ");
            if (parts.length != 2) {
                System.out.println("Format salah. Gunakan format: Nama Menu = Jumlah Porsi");
                continue;
            }

            String itemName = parts[0].trim();
            int quantity;
            try {
                quantity = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                System.out.println("Jumlah harus berupa angka.");
                continue;
            }

            if (menuMap.containsKey(itemName.toLowerCase())) {
                Menu menu = menuMap.get(itemName.toLowerCase());
                orderItems.add(itemName);
                orderQuantities.add(quantity);
                totalCost += menu.getPrice() * quantity;

                // Periksa apakah item termasuk kategori minuman dengan memeriksa kategori
                if (menu.getCategory().equalsIgnoreCase("minuman")) {
                    minumanDipesan.add(itemName);
                }
            } else {
                System.out.println("Menu tidak ditemukan.");
            }
        }

        if (orderItems.isEmpty()) {
            System.out.println("Anda belum memesan apapun.");
            return;
        }

        printReceipt(orderItems, orderQuantities, totalCost, minumanDipesan);
        runOrEnd(scanner);

    }

    public static void printReceipt(List<String> orderItems, List<Integer> orderQuantities, int totalCost, List<String> minumanDipesan) {
        double tax = totalCost * 0.10;
        double serviceFee = 20000;

        // Memanggil DiscountManager untuk menghitung diskon
        double discount = DiscountManager.calculateDiscount(totalCost);

        // Memanggil DiscountManager untuk memeriksa minuman gratis
        String minumanGratis = DiscountManager.checkMinumanGratis(minumanDipesan, totalCost);

        // Menampilkan struk
        System.out.println("\n===== Struk Pesanan =====");
        for (int i = 0; i < orderItems.size(); i++) {
            Menu menu = menuMap.get(orderItems.get(i).toLowerCase());
            int itemTotalPrice = menu.getPrice() * orderQuantities.get(i);
            System.out.println(capitalizeFirst(menuMap.get(orderItems.get(i).toLowerCase()).getName()) + " x " + orderQuantities.get(i) + " = " + formatIDR(itemTotalPrice));
        }

        if (!minumanGratis.isEmpty()) {
            System.out.println("\n" + minumanGratis);
        }

        System.out.println("===========================");
        System.out.println("Total: " + formatIDR(totalCost));
        System.out.println("Pajak (10%): " + formatIDR((int) tax));
        System.out.println("Biaya Pelayanan: " + formatIDR((int) serviceFee));

        double finalCost = totalCost + tax + serviceFee - discount;
        if (discount > 0) {
            System.out.println("Diskon: " + formatIDR((int) discount));
        }
        System.out.println("Total Pembayaran: " + formatIDR((int) finalCost));
    }

    private class DiscountManager {

        // Menghitung diskon berdasarkan harga total
        public static double calculateDiscount(int totalCost) {
            double discount = 0;
            if (totalCost > 100000) {
                discount = totalCost * 0.10;
            }
            return discount;
        }

        // Logika diskon untuk promo beli satu gratis satu pada kategori minuman
        public static String checkMinumanGratis(List<String> minumanDipesan, int totalCost) {
            Set<String> uniqueMinuman = new HashSet<>(minumanDipesan);
            String minumanGratis = "";

            if (uniqueMinuman.size() > 0 && uniqueMinuman.size() < 2 && totalCost > 50000) {
                minumanGratis = "Selamat Anda Mendapatkan 1 Minuman Gratis\n" + capitalizeFirst(formatDataArray(minumanDipesan));
            } else if (uniqueMinuman.size() >= 2 && totalCost > 50000) {
                minumanGratis = "Selamat Anda Mendapatkan 1 Minuman Gratis (Pilih Salah Satu)\n" + capitalizeFirst(formatDataArray(minumanDipesan));
            }

            return minumanGratis;
        }
    }
    public static void runOrEnd(Scanner scanner){
        System.out.println("\nTekan 1 untuk memesan lagi atau 0 untuk keluar.");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            handleOrder(scanner);
        } else {
            System.out.println("Terima kasih telah menggunakan layanan kami!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Menampilkan menu makanan dan minuman tanpa menggunakan pengulangan
        displayMenu();
        handleOrder(scanner);
    }

}
