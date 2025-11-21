package Doanh;

import Nam.*;
import Quan.Quan1.*;
import Quan.Quan3.*;
import Quan.Quan2.*;
import Quan.Menu.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Khởi tạo Repositories
    private static final CustomerRepository customerRepo = new CustomerRepository();
    private static final TableRepository tableRepo = new TableRepository();
    private static final MenuRepository menuRepo = new MenuRepository();
    private static final BookingRepository bookingRepo = new BookingRepository();
    private static final InvoiceRepository invoiceRepo = new InvoiceRepository();

    // Khởi tạo Services
    private static final CustomerService customerService = new CustomerService(customerRepo);
    private static final TableService tableService = new TableService(tableRepo);
    private static final MenuService menuService = new MenuService(menuRepo);
    private static final BookingService bookingService = new BookingService(bookingRepo);
    private static final InvoiceService invoiceService = new InvoiceService(invoiceRepo);
    // CHỨC NĂNG MỚI: Khởi tạo ReportService
    private static final ReportService reportService = new ReportService(invoiceRepo);


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
            System.out.println("5. Quản lý Khách hàng"); // THÊM MỚI
            System.out.println("6. Thống kê & Báo cáo Doanh thu"); // THÊM MỚI
            System.out.println("0. Thoát & Lưu dữ liệu");
            System.out.print("Chọn chức năng: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": handleTableManagement(); break;
                    case "2": handleMenuManagement(); break;
                    case "3": handleBooking(); break;
                    case "4": handleInvoiceManagement(); break;
                    case "5": handleCustomerManagement(); break; // THÊM MỚI
                    case "6": handleReportManagement(); break; // THÊM MỚI
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
// --- CHỨC NĂNG MỚI: QUẢN LÝ KHÁCH HÀNG (CRUD + EDIT) ---
// ----------------------------------------------------------------------
    private static void handleCustomerManagement() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ KHÁCH HÀNG ---");
            System.out.println("1. Hiển thị tất cả Khách hàng");
            System.out.println("2. Thêm Khách hàng mới");
            System.out.println("3. Sửa thông tin Khách hàng (theo ID)"); // CHỨC NĂNG SỬA
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break;

            switch (choice) {
                case "1":
                    System.out.println("--- Danh sách Khách hàng ---");
                    customerService.getAll().forEach(c -> {
                        System.out.printf("ID: %s | Tên: %s | SĐT: %s | Tuổi: %d\n", c.getId(), c.getName(), c.getPhone(), c.getAge());
                    });
                    break;
                case "2":
                    try {
                        String id = UUID.randomUUID().toString().substring(0, 4);
                        System.out.print("Tên khách hàng: "); String name = scanner.nextLine();
                        System.out.print("SĐT: "); String phone = scanner.nextLine();
                        System.out.print("Tuổi: "); int age = Integer.parseInt(scanner.nextLine());
                        customerService.add(new Customer(id, name, phone, age));
                        customerRepo.save();
                        System.out.println("Thêm khách hàng thành công! ID: " + id);
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Tuổi phải là số nguyên.");
                    }
                    break;
                case "3": // CHỨC NĂNG SỬA
                    System.out.print("Nhập ID Khách hàng cần sửa: ");
                    String updateId = scanner.nextLine();
                    try {
                        Customer customerToUpdate = customerService.find(updateId);

                        System.out.println("--- Sửa Khách hàng: " + customerToUpdate.getName() + " ---");

                        System.out.printf("Tên mới (Hiện tại: %s, Bỏ qua nếu không đổi): ", customerToUpdate.getName());
                        String newName = scanner.nextLine();

                        System.out.printf("SĐT mới (Hiện tại: %s, Bỏ qua nếu không đổi): ", customerToUpdate.getPhone());
                        String newPhone = scanner.nextLine();

                        System.out.printf("Tuổi mới (Hiện tại: %d, Bỏ qua nếu không đổi): ", customerToUpdate.getAge());
                        String newAgeStr = scanner.nextLine();
                        int newAge = customerToUpdate.getAge(); // Giữ giá trị cũ

                        if (!newAgeStr.trim().isEmpty()) {
                            newAge = Integer.parseInt(newAgeStr);
                        }

                        // Gọi phương thức edit trong Service
                        customerService.edit(updateId,
                                newName.trim().isEmpty() ? customerToUpdate.getName() : newName,
                                newPhone.trim().isEmpty() ? customerToUpdate.getPhone() : newPhone,
                                newAge);

                        customerRepo.save();
                        System.out.println("Sửa Khách hàng thành công và đã lưu vào tệp.");

                    } catch (CustomerNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Tuổi nhập vào không hợp lệ.");
                    }
                    break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }


    // ----------------------------------------------------------------------
// --- CHỨC NĂNG 1: QUẢN LÝ BÀN ĂN (BỔ SUNG EDIT) ---
// ----------------------------------------------------------------------
    private static void handleTableManagement() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ BÀN ĂN ---");
            System.out.println("1. Hiển thị tất cả Bàn");
            System.out.println("2. Thêm bàn mới");
            System.out.println("3. Xóa bàn theo ID");
            System.out.println("4. Sửa trạng thái Bàn theo ID"); // THÊM CHỨC NĂNG SỬA
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break;

            switch (choice) {
                // ... (Case 1, 2, 3 giữ nguyên)
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
                        tableRepo.save();
                        System.out.println("Thêm bàn thành công và đã lưu vào tệp.");
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi: Số chỗ ngồi phải là số nguyên.");
                    }
                    break;
                case "3":
                    System.out.print("Nhập ID bàn cần xóa: "); String removeId = scanner.nextLine();
                    try {
                        tableService.remove(removeId);
                        tableRepo.save();
                        System.out.println("Xóa bàn thành công và đã lưu vào tệp.");
                    } catch (TableNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    }
                    break;
                case "4": // CHỨC NĂNG SỬA TRẠNG THÁI BÀN
                    System.out.print("Nhập ID bàn cần sửa trạng thái: ");
                    String updateId = scanner.nextLine();
                    try {
                        Table tableToUpdate = tableService.find(updateId);

                        System.out.println("--- Sửa trạng thái Bàn: " + tableToUpdate.getId() + " (Hiện tại: " + tableToUpdate.getStatus() + ") ---");
                        System.out.println("Chọn trạng thái mới:");
                        System.out.println("  1. AVAILABLE (Trống)");
                        System.out.println("  2. BOOKED (Đã đặt)");
                        System.out.println("  3. OCCUPIED (Đang dùng)");
                        System.out.print("Nhập số trạng thái mới (1, 2, hoặc 3): ");
                        String statusChoice = scanner.nextLine();

                        Table.Status newStatus;
                        switch (statusChoice) {
                            case "1": newStatus = Table.Status.AVAILABLE; break;
                            case "2": newStatus = Table.Status.BOOKED; break;
                            case "3": newStatus = Table.Status.OCCUPIED; break;
                            default:
                                System.err.println("Lỗi: Lựa chọn trạng thái không hợp lệ. Thao tác bị hủy.");
                                return;
                        }

                        // Gọi phương thức edit trong Service (dùng lại cho việc set status)
                        tableToUpdate.setStatus(newStatus);
                        tableRepo.save();
                        System.out.println("Cập nhật trạng thái Bàn thành công và đã lưu vào tệp.");

                    } catch (TableNotFoundException e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Lỗi: " + e.getMessage());
                    }
                    break;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // ----------------------------------------------------------------------
// --- CHỨC NĂNG MỚI: THỐNG KÊ & BÁO CÁO DOANH THU ---
// ----------------------------------------------------------------------
    private static void handleReportManagement() {
        while (true) {
            System.out.println("\n--- THỐNG KÊ & BÁO CÁO DOANH THU ---");
            System.out.println("1. Doanh thu theo khoảng ngày");
            System.out.println("2. Doanh thu theo tháng (trong năm)");
            System.out.println("3. Top các món bán chạy nhất");
            System.out.println("0. Quay lại Menu Chính");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            if ("0".equals(choice)) break;

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Ngày bắt đầu (YYYY-MM-DD): "); String startStr = scanner.nextLine();
                        System.out.print("Ngày kết thúc (YYYY-MM-DD): "); String endStr = scanner.nextLine();
                        LocalDate startDate = LocalDate.parse(startStr);
                        LocalDate endDate = LocalDate.parse(endStr);

                        double totalRevenue = reportService.getTotalRevenue(startDate, endDate);
                        System.out.printf("=> TỔNG DOANH THU từ %s đến %s: **%.0f VND**\n", startStr, endStr, totalRevenue);
                        break;

                    case "2":
                        System.out.print("Nhập năm cần thống kê (YYYY): "); int year = Integer.parseInt(scanner.nextLine());
                        Map<Month, Double> monthlyRevenue = reportService.getMonthlyRevenue(year);
                        System.out.println("--- DOANH THU THEO THÁNG NĂM " + year + " ---");
                        if (monthlyRevenue.isEmpty()) {
                            System.out.println("Không có hóa đơn nào trong năm " + year + ".");
                        } else {
                            monthlyRevenue.forEach((month, total) ->
                                    System.out.printf(" - %-10s: %.0f VND%n", month.toString(), total)
                            );
                        }
                        break;

                    case "3":
                        System.out.print("Nhập số lượng món Top (ví dụ: 5): "); int limit = Integer.parseInt(scanner.nextLine());
                        Map<String, Integer> topItems = reportService.getTopSellingItems(limit);
                        System.out.println("--- TOP " + limit + " MÓN BÁN CHẠY NHẤT ---");
                        int rank = 1;
                        for (Map.Entry<String, Integer> entry : topItems.entrySet()) {
                            System.out.printf(" %d. %-20s: %d lượt bán%n", rank++, entry.getKey(), entry.getValue());
                        }
                        break;

                    default: System.out.println("Lựa chọn không hợp lệ.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: Dữ liệu nhập vào phải là số nguyên hoặc số thực.");
            } catch (DateTimeParseException e) {
                System.err.println("Lỗi: Định dạng ngày tháng không hợp lệ (cần YYYY-MM-DD).");
            }
        }
    }


// ----------------------------------------------------------------------
// --- CÁC CHỨC NĂNG CÒN LẠI (GIỮ NGUYÊN) ---
// ----------------------------------------------------------------------

    // --- CHỨC NĂNG 2: QUẢN LÝ THỰC ĐƠN ---
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

                        // Sửa Giá
                        System.out.printf("Giá mới (Hiện tại: %.0f, Bỏ qua nếu không đổi): ", itemToUpdate.getPrice());
                        String newPriceStr = scanner.nextLine();

                        // Sửa Giảm giá
                        System.out.printf("Giảm giá mới (0-1.0) (Hiện tại: %.1f, Bỏ qua nếu không đổi): ", itemToUpdate.getDiscount());
                        String newDiscStr = scanner.nextLine();

                        // Cập nhật các trường
                        if (!newName.trim().isEmpty()) {
                            itemToUpdate.setName(newName);
                        }
                        if (!newPriceStr.trim().isEmpty()) {
                            itemToUpdate.setPrice(Double.parseDouble(newPriceStr));
                        }
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

    // --- CHỨC NĂNG 3: ĐẶT BÀN ---
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

    // --- CHỨC NĂNG 4: QUẢN LÝ HÓA ĐƠN ---
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