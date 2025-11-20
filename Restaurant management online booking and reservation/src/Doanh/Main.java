package Doanh;

import Nam.*;
import Quan.Quan1.*;
import Quan.Quan3.*;
import Quan.Quan2.*;
import Quan.Menu.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Khởi tạo Repositories
    private static final CustomerRepository customerRepo = new CustomerRepository();
    private static final TableRepository tableRepo = new TableRepository();
    private static final MenuRepository menuRepo = new MenuRepository();
    private static final BookingRepository1 bookingRepo = new BookingRepository1();
    private static final InvoiceRepository invoiceRepo = new InvoiceRepository();

    // Khởi tạo Services
    private static final CustomerService customerService = new CustomerService(customerRepo);
    private static final TableService tableService = new TableService(tableRepo);
    private static final MenuService menuService = new MenuService(menuRepo);
    private static final BookingService bookingService = new BookingService(bookingRepo);
    private static final InvoiceService invoiceService = new InvoiceService(invoiceRepo);

    private static void loadAllData() {
        System.out.println("--- Đang nạp dữ liệu từ CSV...");
        customerRepo.load();
        tableRepo.load();
        menuRepo.load();
        bookingRepo.load();
        invoiceRepo.load();
        System.out.println("--- Nạp dữ liệu hoàn tất. ---");
    }

    private static void saveAllData() {
        System.out.println("--- Đang lưu dữ liệu ra CSV...");
        customerRepo.save();
        tableRepo.save();
        menuRepo.save();
        bookingRepo.save();
        invoiceRepo.save();
        System.out.println("--- Lưu dữ liệu hoàn tất. ---");
    }

    public static void main(String[] args) {
        loadAllData();

        while (true) {
            System.out.println("\n--- HỆ THỐNG QUẢN LÝ NHÀ HÀNG ---");
            System.out.println("1. Quản lý Bàn ăn");
            System.out.println("2. Quản lý Thực đơn");
            System.out.println("3. Đặt bàn");
            System.out.println("4. Quản lý Hóa đơn");
            System.out.println("0. Thoát & Lưu dữ liệu");
            System.out.print("Chọn chức năng: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": handleTableManagement(); break;
                    case "2": handleMenuManagement(); break;
                    case "3": handleBooking(); break;
                    case "4": handleInvoiceManagement(); break;
                    case "0":
                        saveAllData();
                        System.out.println("Ứng dụng đã thoát.");
                        return;
                    default: System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (Exception e) {
                System.err.println("Đã xảy ra lỗi không xác định: " + e.getMessage());
            }
        }
    }

// ----------------------------------------------------------------------

    // --- CHỨC NĂNG 1: QUẢN LÝ BÀN ĂN (CÓ LOOP VÀ QUAY LẠI) ---
    private static void handleTableManagement() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ BÀN ĂN ---");
            System.out.println("1. Hiển thị tất cả Bàn");
            System.out.println("2. Thêm bàn mới");
            System.out.println("3. Xóa bàn theo ID");
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break; // Quay lại main menu

            switch (choice) {
                case "1":
                    System.out.println("--- Danh sách Bàn ---");
                    tableService.getAll().forEach(t -> {
                        System.out.printf("ID: %s | Loại: %s | Chỗ ngồi: %d | Trạng thái: %s | Phụ phí: %.0f\n",
                                t.getId(), t.getClass().getSimpleName().replace("Table", ""), t.getSeats(), t.getStatus(), t.getSurcharge());
                    });
                    break;
                case "2":
                    try {
                        System.out.print("ID bàn mới: "); String id = scanner.nextLine();
                        System.out.print("Số chỗ ngồi: "); int seats = Integer.parseInt(scanner.nextLine());

                        System.out.println("Chọn loại bàn:");
                        System.out.println("  1. StandardTable (Phụ phí: 0)");
                        System.out.println("  2. VipTable (Phụ phí: 50,000)");
                        System.out.print("Nhập số loại (1 hoặc 2): ");
                        String typeChoice = scanner.nextLine();

                        Table newTable;
                        if ("1".equals(typeChoice)) {
                            newTable = new StandardTable(id, seats, Table.Status.AVAILABLE);
                        } else if ("2".equals(typeChoice)) {
                            newTable = new VipTable(id, seats, Table.Status.AVAILABLE);
                        } else {
                            System.err.println("Lỗi: Lựa chọn loại bàn không hợp lệ. Thao tác bị hủy.");
                            break;
                        }

                        tableService.add(newTable);
                        tableRepo.save(); // LƯU NGAY LẬP TỨC
                        System.out.println("Thêm bàn thành công và đã lưu vào tệp.");
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Số chỗ ngồi phải là số nguyên.");
                    }
                    break;
                case "3":
                    System.out.print("Nhập ID bàn cần xóa: "); String removeId = scanner.nextLine();
                    try {
                        tableService.remove(removeId);
                        tableRepo.save(); // LƯU NGAY LẬP TỨC
                        System.out.println("Xóa bàn thành công và đã lưu vào tệp.");
                    } catch (TableNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    }
                    break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

// ----------------------------------------------------------------------

    // --- CHỨC NĂNG 2: QUẢN LÝ THỰC ĐƠN (CÓ LOOP VÀ QUAY LẠI) ---
    private static void handleMenuManagement() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ THỰC ĐƠN ---");
            System.out.println("1. Hiển thị tất cả Menu");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Sửa thông tin món ăn (theo ID)");
            System.out.println("4. Xóa món theo ID");
            System.out.println("5. Tìm kiếm món theo Tên");
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break; // Quay lại main menu

            switch (choice) {
                case "1":
                    System.out.println("--- Danh sách Thực đơn ---");
                    menuService.getAll().forEach(m -> {
                        System.out.printf("ID: %s | Tên: %s | Loại: %s | Giá: %.0f | KM: %.0f%%\n",
                                m.getId(), m.getName(), m.getType(), m.getPrice(), m.getDiscount() * 100);
                    });
                    break;
                case "2":
                    try {
                        System.out.print("ID món mới: "); String id = scanner.nextLine();
                        System.out.print("Tên món: "); String name = scanner.nextLine();
                        System.out.print("Giá: "); double price = Double.parseDouble(scanner.nextLine());
                        System.out.print("Giảm giá (0-1.0): "); double disc = Double.parseDouble(scanner.nextLine());

                        System.out.println("Chọn loại:");
                        System.out.println("  1. Food");
                        System.out.println("  2. Drink");
                        System.out.print("Nhập số loại (1 hoặc 2): ");
                        String typeChoice = scanner.nextLine();

                        MenuItem newItem;
                        if ("1".equals(typeChoice)) {
                            newItem = new Food(id, name, price, disc);
                        } else if ("2".equals(typeChoice)) {
                            newItem = new Drink(id, name, price, disc);
                        } else {
                            System.err.println("Lỗi: Lựa chọn loại không hợp lệ. Thao tác bị hủy.");
                            break;
                        }

                        menuService.add(newItem);
                        menuRepo.save(); // LƯU NGAY LẬP TỨC
                        System.out.println("Thêm món thành công và đã lưu vào tệp.");
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Giá hoặc Giảm giá không hợp lệ.");
                    }
                    break;
                case "3": // CHỨC NĂNG SỬA THÔNG TIN
                    System.out.print("Nhập ID món cần sửa: ");
                    String updateId = scanner.nextLine();
                    try {
                        MenuItem itemToUpdate = menuService.find(updateId);

                        System.out.println("--- Sửa món: " + itemToUpdate.getName() + " ---");

                        // Sửa Tên
                        System.out.printf("Tên mới (Hiện tại: %s, Bỏ qua nếu không đổi): ", itemToUpdate.getName());
                        String newName = scanner.nextLine();
                        if (!newName.trim().isEmpty()) {
                            itemToUpdate.setName(newName);
                        }

                        // Sửa Giá
                        System.out.printf("Giá mới (Hiện tại: %.0f, Bỏ qua nếu không đổi): ", itemToUpdate.getPrice());
                        String newPriceStr = scanner.nextLine();
                        if (!newPriceStr.trim().isEmpty()) {
                            itemToUpdate.setPrice(Double.parseDouble(newPriceStr));
                        }

                        // Sửa Giảm giá
                        System.out.printf("Giảm giá mới (0-1.0) (Hiện tại: %.1f, Bỏ qua nếu không đổi): ", itemToUpdate.getDiscount());
                        String newDiscStr = scanner.nextLine();
                        if (!newDiscStr.trim().isEmpty()) {
                            itemToUpdate.setDiscount(Double.parseDouble(newDiscStr));
                        }

                        menuRepo.save(); // LƯU NGAY LẬP TỨC
                        System.out.println("Sửa món thành công và đã lưu vào tệp.");

                    } catch (MenuItemNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Giá hoặc Giảm giá nhập vào không hợp lệ.");
                    }
                    break;
                case "4": // XÓA MÓN ĂN
                    System.out.print("Nhập ID món cần xóa: "); String removeId = scanner.nextLine();
                    try {
                        menuService.remove(removeId);
                        menuRepo.save(); // LƯU NGAY LẬP TỨC
                        System.out.println("Xóa món thành công và đã lưu vào tệp.");
                    } catch (MenuItemNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    }
                    break;
                case "5": // TÌM KIẾM MÓN ĂN
                    System.out.print("Nhập tên/từ khóa: "); String keyword = scanner.nextLine();
                    List<MenuItem> results = menuService.searchByName(keyword);
                    System.out.println("--- Kết quả Tìm kiếm ---");
                    results.forEach(m -> System.out.printf("ID: %s | Tên: %s | Giá: %.0f\n", m.getId(), m.getName(), m.getPrice()));
                    break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

// ----------------------------------------------------------------------

    // --- CHỨC NĂNG 3: ĐẶT BÀN (SINGLE ACTION) ---
    private static void handleBooking() {
        System.out.println("\n--- ĐẶT BÀN TRỰC TUYẾN ---");
        // BƯỚC 1: Chọn bàn (Chỉ hiển thị bàn AVAILABLE hoặc BOOKED để đặt thêm)
        System.out.println("--- Các bàn KHẢ DỤNG: ---");
        List<Table> availableTables = tableService.getAll().stream()
                .filter(t -> t.getStatus() != Table.Status.OCCUPIED)
                .toList();

        if (availableTables.isEmpty()) {
            System.out.println("Không có bàn trống nào để đặt.");
            return;
        }

        availableTables.forEach(t -> {
            System.out.printf("ID: %s | Loại: %s | Chỗ ngồi: %d | Trạng thái: %s\n",
                    t.getId(), t.getClass().getSimpleName().replace("Table", ""), t.getSeats(), t.getStatus());
        });

        System.out.print("Chọn ID bàn muốn đặt (hoặc '0' để hủy): ");
        String tableId = scanner.nextLine();

        if ("0".equals(tableId)) return; // Hủy đặt bàn

        Table selectedTable;

        try {
            selectedTable = tableService.find(tableId);
        } catch (TableNotFoundException e) {
            System.err.println("Lỗi: ID bàn không tồn tại.");
            return;
        }

        // BƯỚC 2: Nhập thông tin đặt bàn
        String bookingId = UUID.randomUUID().toString().substring(0, 4);
        System.out.print("Tên khách hàng: "); String name = scanner.nextLine();
        System.out.print("SĐT khách hàng: "); String phone = scanner.nextLine();
        System.out.print("Ngày đặt (YYYY-MM-DD): "); String dateStr = scanner.nextLine();
        System.out.print("Giờ đặt (HH:MM): "); String timeStr = scanner.nextLine();

        try {
            System.out.print("Số lượng người (Pax): "); int pax = Integer.parseInt(scanner.nextLine());

            Booking newBooking = new Booking(bookingId, tableId, name, phone, dateStr, timeStr, pax);

            bookingService.addBooking(newBooking);

            selectedTable.setStatus(Table.Status.BOOKED);
            tableRepo.save();
            bookingRepo.save(); // LƯU BOOKING

            System.out.println("Đặt bàn thành công! Mã đặt bàn: " + bookingId);
        } catch (TableAlreadyBookedException e) {
            System.err.println("Lỗi: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Lỗi: Số lượng người không hợp lệ.");
        } catch (Exception e) {
            System.err.println("Lỗi định dạng ngày/giờ hoặc lỗi khác: " + e.getMessage());
        }
    }

// ----------------------------------------------------------------------

    // --- CHỨC NĂNG 4: QUẢN LÝ HÓA ĐƠN (CÓ LOOP VÀ QUAY LẠI) ---
    private static void handleInvoiceManagement() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ HÓA ĐƠN ---");
            System.out.println("1. Tạo Hóa đơn mới");
            System.out.println("2. Xem danh sách Hóa đơn đã thanh toán");
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break; // Quay lại Menu Chính

            switch (choice) {
                case "2":
                    System.out.println("--- Danh sách Hóa đơn ---");
                    invoiceService.getAll().forEach(i -> {
                        System.out.printf("ID: %s | Bàn: %s | Khách: %s | Phụ phí: %.0f | Tổng tiền: %.0f\n",
                                i.getId(), i.getTableId(), i.getCustomerName(), i.getSurcharge(), i.total());
                    });
                    break;

                case "1":
                    // --- TẠO HÓA ĐƠN MỚI ---
                    String invoiceId = UUID.randomUUID().toString().substring(0, 4);

                    try {
                        // 1. Nhập thông tin cơ bản
                        System.out.print("ID Bàn thanh toán (hoặc '0' để hủy): "); String tableId = scanner.nextLine();

                        if ("0".equals(tableId)) break; // Hủy tạo hóa đơn

                        Table currentTable = tableService.find(tableId);

                        // Thiết lập trạng thái bàn đang phục vụ (hoặc OCCUPIED nếu cần)
                        // Giả định bàn đã được phục vụ (OCCUPIED) hoặc BOOKED
                        currentTable.setStatus(Table.Status.OCCUPIED);

                        System.out.print("Tên khách hàng: "); String customerName = scanner.nextLine();
                        System.out.print("Chiết khấu chung (0.0 - 1.0): "); double discount = Double.parseDouble(scanner.nextLine());

                        if (discount < 0 || discount > 1.0) {
                            System.err.println("Lỗi: Giảm giá phải nằm trong khoảng từ 0.0 đến 1.0. Hóa đơn bị hủy.");
                            currentTable.setStatus(Table.Status.AVAILABLE); // Hoàn tác trạng thái
                            break;
                        }

                        // Khởi tạo hóa đơn
                        Invoice newInvoice = new Invoice(invoiceId, tableId, customerName,
                                LocalDateTime.now().toString(), currentTable.getSurcharge(), discount);

                        // 2. Thêm món vào hóa đơn
                        System.out.println("\n--- THÊM MÓN VÀO HÓA ĐƠN (Nhập 'done' để hoàn tất) ---");
                        while (true) {
                            // Hiển thị menu nhanh
                            menuService.getAll().forEach(m -> System.out.printf("ID: %s | Tên: %s | Giá: %.0f\n", m.getId(), m.getName(), m.getPrice()));
                            System.out.print("Nhập ID món hoặc 'done': ");
                            String itemId = scanner.nextLine();
                            if ("done".equalsIgnoreCase(itemId)) break;

                            try {
                                MenuItem item = menuService.find(itemId);
                                System.out.print("Số lượng: "); int quantity = Integer.parseInt(scanner.nextLine());
                                newInvoice.addLine(new InvoiceLine(item, quantity));
                                System.out.println("Đã thêm " + item.getName() + " x" + quantity);
                            } catch (MenuItemNotFoundException e) {
                                System.err.println("Lỗi: " + e.getMessage());
                            } catch (NumberFormatException e) {
                                System.err.println("Lỗi: Số lượng không hợp lệ.");
                            }
                        }

                        // 3. Tính và lưu
                        System.out.println("\n--- TỔNG KẾT HÓA ĐƠN ---");
                        double subtotal = 0;
                        for (InvoiceLine line : newInvoice.getLines()) {
                            subtotal += line.getTotal();
                        }

                        System.out.printf("Tạm tính món ăn (sau KM món): %.0f\n", subtotal);
                        System.out.printf("Phụ phí Bàn: %.0f\n", newInvoice.getSurcharge());

                        // Cần tính lại total trong Invoice.java (phần này giả định Total là Tạm tính + Phụ phí - Chiết khấu)
                        double finalTotal = subtotal + newInvoice.getSurcharge() - (subtotal + newInvoice.getSurcharge()) * newInvoice.getDiscount();

                        System.out.printf("Giảm giá chung (%.0f%%): -%.0f\n", newInvoice.getDiscount() * 100, (subtotal + newInvoice.getSurcharge()) * newInvoice.getDiscount());
                        System.out.printf("**TỔNG THANH TOÁN (Final): %.0f**\n", finalTotal);

                        invoiceService.addInvoice(newInvoice);
                        invoiceRepo.save(); // LƯU HÓA ĐƠN
                        currentTable.setStatus(Table.Status.AVAILABLE); // Bàn trở lại trạng thái trống
                        tableRepo.save(); // Lưu trạng thái bàn đã được cập nhật
                        System.out.println("Hóa đơn đã được lưu và bàn đã được giải phóng.");

                    } catch (TableNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Chiết khấu hoặc dữ liệu số không hợp lệ. Hóa đơn bị hủy.");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }
}