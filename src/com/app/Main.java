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
    private static Map<String, Menu> menuMap = new LinkedHashMap<>();
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

    public static void home(Scanner scanner) {
        System.out.println("==== Open Food ====");
        System.out.println("1. Pemesanan");
        System.out.println("2. Manajemen Menu");
        System.out.println("0. Keluar");
        System.out.print("Pilih Menu: ");

        String input = scanner.nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            if (choice == 1) {
                displayMenu();
                handleOrder(scanner);
            } else if (choice == 2) {
                manageMenu(scanner);
            }
            else if(choice == 0){
                return;
            }
            else {
                System.out.println("Menu tidak ada. Silakan coba lagi.");
                home(scanner);
            }
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka. Silakan coba lagi.");
            home(scanner);
        }
    }

    //Manajemen Menu
    public static void manageMenu(Scanner scanner){
        while (true) {
            System.out.println("\n==== Manajemen Menu ====");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("0. Kembali ke Home");
            System.out.println("00. Keluar");
            System.out.print("Pilih Opsi: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    addMenu(scanner);
                    break;
                case "2":
                    updateMenuPrice(scanner);
                    break;
                case "3":
                    deleteMenu(scanner);
                    break;
                case "0":
                    home(scanner);
                    return;
                case "00":
                    return;
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    };

    public static void addMenu(Scanner scanner) {
        System.out.println("\n==== Tambah Menu ====");

        String name = null;
        int price = 0;
        String category = null;

        int step = 1;

        while (true) {
            switch (step) {
                case 1:
                    name = inputWithBack(scanner, "Masukkan Nama Menu (atau ketik '0' untuk kembali): ");
                    if (name == null) return;
                    step = 2;
                    break;

                case 2:
                    String priceInput = inputWithBack(scanner, "Masukkan Harga Menu (atau ketik '0' untuk kembali ke Nama Menu): ");
                    if (priceInput == null) {
                        step = 1;
                        break;
                    }
                    try {
                        price = Integer.parseInt(priceInput.trim());
                        step = 3;
                    } catch (NumberFormatException e) {
                        System.out.println("Harga harus berupa angka. Silakan coba lagi.");
                    }
                    break;

                case 3:
                    category = inputWithBack(scanner, "Masukkan kategori (makanan/minuman, ketik '0' untuk kembali ke Harga Menu): ");
                    if (category == null) {
                        step = 2;
                        break;
                    }
                    step = 4;
                    break;

                case 4:
                    System.out.println("\n==== Konfirmasi Menu ====");
                    System.out.println("Nama Menu : " + capitalizeFirst(name));
                    System.out.println("Harga Menu: " + formatIDR(price));
                    System.out.println("Kategori  : " + capitalizeFirst(category));
                    String confirm = inputWithBack(scanner, "Ketik 'y' untuk simpan, '0' untuk kembali ke Kategori, atau 'n' untuk batal: ");
                    if (confirm == null) {
                        step = 3;
                        break;
                    }
                    if (confirm.equalsIgnoreCase("y")) {
                        menuMap.put(name.toLowerCase(), new Menu(capitalizeFirst(name), price, category.toLowerCase()));
                        System.out.println("Menu berhasil ditambahkan!");
                        return;
                    } else if (confirm.equalsIgnoreCase("n")) {
                        System.out.println("Penambahan menu dibatalkan.");
                        return;
                    } else {
                        System.out.println("Input tidak valid. Silakan coba lagi.");
                    }
                    break;
            }
        }
    }

    private static String inputWithBack(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            return null;
        }
        return input;
    }

    //Update Data
    public static void updateMenuPrice(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Ubah Harga Menu ====");
            System.out.println("Daftar Menu:");

            List<Menu> menuList = new ArrayList<>(menuMap.values());
            for (int i = 0; i < menuList.size(); i++) {
                Menu menu = menuList.get(i);
                System.out.println((i + 1) + ". " + capitalizeFirst(menu.getName()) + " (" + capitalizeFirst(menu.getCategory()) + ") - " + formatIDR(menu.getPrice()));
            }

            System.out.println("\n0. Kembali ke Manajemen Menu");
            System.out.print("Masukkan nomor menu yang ingin diubah harganya: ");

            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return;
            }

            try {
                int menuIndex = Integer.parseInt(input) - 1;

                if (menuIndex < 0 || menuIndex >= menuList.size()) {
                    System.out.println("Nomor tidak valid. Silakan coba lagi.");
                    continue;
                }

                // Ambil menu berdasarkan nomor
                Menu menuToUpdate = menuList.get(menuIndex);
                System.out.println("Menu Dipilih: " + capitalizeFirst(menuToUpdate.getName()) + " - Harga Saat Ini: " + formatIDR(menuToUpdate.getPrice()));

                // Masukkan harga baru
                System.out.print("Masukkan Harga Baru (atau ketik '0' untuk batal): ");
                String newPriceInput = scanner.nextLine().trim();

                if (newPriceInput.equals("0")) {
                    System.out.println("Perubahan harga dibatalkan.");
                    continue;
                }

                int newPrice = Integer.parseInt(newPriceInput);
                if (newPrice < 0) {
                    System.out.println("Harga tidak boleh negatif. Silakan coba lagi.");
                    continue;
                }

                // Update harga menu
                menuMap.put(menuToUpdate.getName().toLowerCase(), new Menu(menuToUpdate.getName(), newPrice, menuToUpdate.getCategory()));
                System.out.println("Harga berhasil diperbarui untuk " + capitalizeFirst(menuToUpdate.getName()) + " menjadi " + formatIDR(newPrice) + "!");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka. Silakan coba lagi.");
            }
        }
    }

    //Delete Menu
    public static void deleteMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n==== Hapus Menu ====");
            System.out.println("Daftar Menu:");
            List<Menu> menuList = new ArrayList<>(menuMap.values());
            for (int i = 0; i < menuList.size(); i++) {
                Menu menu = menuList.get(i);
                System.out.println((i + 1) + ". " + capitalizeFirst(menu.getName()) + " (" + capitalizeFirst(menu.getCategory()) + ") - " + formatIDR(menu.getPrice()));
            }

            System.out.println("\n0. Kembali ke Manajemen Menu");
            System.out.print("Masukkan nomor menu yang ingin dihapus: ");

            String input = scanner.nextLine().trim();

            // Kembali ke Manajemen Menu jika input "0"
            if (input.equals("0")) {
                return;
            }

            try {
                int menuIndex = Integer.parseInt(input) - 1;

                if (menuIndex < 0 || menuIndex >= menuList.size()) {
                    System.out.println("Nomor tidak valid. Silakan coba lagi.");
                    continue;
                }

                // Konfirmasi penghapusan
                Menu menuToDelete = menuList.get(menuIndex);
                System.out.println("\nApakah Anda yakin ingin menghapus menu berikut?");
                System.out.println("Nama: " + capitalizeFirst(menuToDelete.getName()));
                System.out.println("Kategori: " + capitalizeFirst(menuToDelete.getCategory()));
                System.out.println("Harga: " + formatIDR(menuToDelete.getPrice()));
                System.out.print("Ketik 'y' untuk menghapus atau 'n' untuk batal: ");

                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    menuMap.remove(menuToDelete.getName().toLowerCase());
                    System.out.println("Menu berhasil dihapus!");
                } else {
                    System.out.println("Penghapusan dibatalkan.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka. Silakan coba lagi.");
            }
        }
    }

    // Menampilkan menu makanan dan minuman
    public static void displayMenu() {
        System.out.println("===============================");
        System.out.println("       Menu Makanan           ");
        System.out.println("===============================");
        int counter = 1;
        for (Menu menu : menuMap.values()) {
            if (menu.getCategory().equalsIgnoreCase("makanan")) {
                System.out.println(counter + ". " + capitalizeFirst(menu.getName()) + " - " + formatIDR(menu.getPrice()));
                counter++;
            }
        }

        System.out.println("===============================");
        System.out.println("       Menu Minuman           ");
        System.out.println("===============================");
        counter = 1;
        for (Menu menu : menuMap.values()) {
            if (menu.getCategory().equalsIgnoreCase("minuman")) {
                System.out.println(counter + ". " + capitalizeFirst(menu.getName()) + " - " + formatIDR(menu.getPrice()));
                counter++;
            }
        }
        System.out.println("===============================");
        System.out.println("0. Home");
        System.out.println("00. Keluar");
    }

    //Pesan Menu
    public static void handleOrder(Scanner scanner) {
        System.out.println("Masukkan Pesanan Anda (contoh: Nasi Goreng = 2). Ketik 'selesai' untuk menyelesaikan pesanan:");
        List<String> orderItems = new ArrayList<>();
        List<Integer> orderQuantities = new ArrayList<>();
        List<String> minumanDipesan = new ArrayList<>();
        int totalCost = 0;
//        int maxOrder = 4;

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

            if (input.equalsIgnoreCase("0")) {
                home(scanner);
                return;
            }

            if (input.equalsIgnoreCase("00")) {
                return;
            }

//            if (orderItems.size() >= maxOrder) {
//                System.out.println("Anda sudah memesan maksimal " + maxOrder + " item. Pesanan telah ditutup.");
//                break;
//            }

            String[] parts = input.split(" = ");
            if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
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
        home(scanner);
    }

}
