import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import javax.swing.Timer;   // ← ADD THIS LINE
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

/**
 * HOSTEL MANAGEMENT SYSTEM - Modern Swing UI
 *
 * OOP Concepts Demonstrated:
 * 1. Class & Object            2. Encapsulation
 * 3. Abstraction               4. Inheritance
 * 5. Polymorphism               6. Method Overloading
 * 7. Method Overriding          8. Interfaces
 * 9. Enums                     10. Composition / Association
 * 11. Exception Handling       12. Collections
 * 13. File Handling            14. Generics
 * 15. Static Members           16. Final Members
 * 17. Lambda / Comparator      18. Streams
 * 19. Inner Classes            20. Anonymous Classes
 * 21. Swing / AWT GUI          22. Event Handling
 * 23. Custom Painting          24. MVC Pattern
 */
public class HostelManagementSystem {

    // ============================================================
    // THEME & COLORS - Modern Design System
    // ============================================================
    static final class Theme {
        // Primary Colors
        static final Color PRIMARY = new Color(79, 70, 229);        // Indigo
        static final Color PRIMARY_DARK = new Color(55, 48, 163);
        static final Color PRIMARY_LIGHT = new Color(129, 120, 248);
        static final Color PRIMARY_BG = new Color(238, 242, 255);

        // Secondary Colors
        static final Color SECONDARY = new Color(16, 185, 129);     // Emerald
        static final Color SECONDARY_DARK = new Color(5, 150, 105);

        // Accent Colors
        static final Color ACCENT_ORANGE = new Color(245, 158, 11);
        static final Color ACCENT_RED = new Color(239, 68, 68);
        static final Color ACCENT_BLUE = new Color(59, 130, 246);
        static final Color ACCENT_PINK = new Color(236, 72, 153);
        static final Color ACCENT_TEAL = new Color(20, 184, 166);
        static final Color ACCENT_PURPLE = new Color(139, 92, 246);

        // Neutrals
        static final Color BG_MAIN = new Color(248, 250, 252);
        static final Color BG_CARD = Color.WHITE;
        static final Color BG_SIDEBAR = new Color(30, 27, 75);
        static final Color BG_SIDEBAR_HOVER = new Color(55, 48, 163);
        static final Color BG_INPUT = new Color(248, 250, 252);

        static final Color TEXT_PRIMARY = new Color(15, 23, 42);
        static final Color TEXT_SECONDARY = new Color(100, 116, 139);
        static final Color TEXT_LIGHT = new Color(148, 163, 184);
        static final Color TEXT_WHITE = Color.WHITE;
        static final Color TEXT_SIDEBAR = new Color(199, 210, 254);

        static final Color BORDER = new Color(226, 232, 240);
        static final Color BORDER_FOCUS = PRIMARY;
        static final Color DIVIDER = new Color(241, 245, 249);

        // Status Colors
        static final Color SUCCESS = new Color(34, 197, 94);
        static final Color WARNING = new Color(234, 179, 8);
        static final Color DANGER = new Color(239, 68, 68);
        static final Color INFO = new Color(59, 130, 246);

        // Fonts
        static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
        static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 20);
        static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
        static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
        static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
        static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
        static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 12);
        static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
        static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 14);
        static final Font FONT_SIDEBAR_HEADER = new Font("Segoe UI", Font.BOLD, 11);
        static final Font FONT_STAT_VALUE = new Font("Segoe UI", Font.BOLD, 32);
        static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
        static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 12);
        static final Font FONT_TABLE_CELL = new Font("Segoe UI", Font.PLAIN, 13);

        static final int BORDER_RADIUS = 12;
        static final int CARD_PADDING = 24;
        static final int SIDEBAR_WIDTH = 260;

        private Theme() {}
    }

    // ============================================================
    // ENUMS
    // ============================================================
    enum Role { ADMIN, RECEPTIONIST, RESIDENT }
    enum AccountStatus { ACTIVE, INACTIVE }
    enum BedStatus { AVAILABLE, MAINTENANCE }
    enum RoomStatus { AVAILABLE, MAINTENANCE }
    enum RequestStatus { PENDING, APPROVED, REJECTED }
    enum AllocationStatus { ACTIVE, VACATED }
    enum BillStatus { UNPAID, PARTIALLY_PAID, PAID }
    enum PaymentStatus { SUCCESS, PENDING, FAILED }
    enum PaymentMethod { CASH, UPI, CARD, BANK_TRANSFER }

    // ============================================================
    // CUSTOM EXCEPTIONS
    // ============================================================
    static class HostelException extends Exception {
        public HostelException(String msg) { super(msg); }
    }
    static class NotFoundException extends HostelException {
        public NotFoundException(String msg) { super(msg); }
    }
    static class ConflictException extends HostelException {
        public ConflictException(String msg) { super(msg); }
    }
    static class ValidationException extends HostelException {
        public ValidationException(String msg) { super(msg); }
    }

    // ============================================================
    // INTERFACE
    // ============================================================
    interface Identifiable { long getId(); }

    // ============================================================
    // ABSTRACT CLASS
    // ============================================================
    static abstract class Person implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        protected long id;
        protected String fullName;
        protected String phone;

        public Person(long id, String fullName, String phone) {
            this.id = id;
            this.fullName = fullName;
            this.phone = phone;
        }

        @Override public long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getPhone() { return phone; }
        public abstract String getPersonType();

        @Override
        public String toString() {
            return fullName + " (" + getPersonType() + ")";
        }
    }

    // ============================================================
    // USER CLASS
    // ============================================================
    static class User extends Person {
        private static final long serialVersionUID = 1L;
        private String email;
        private String password;
        private Role role;
        private AccountStatus status;

        public User(long id, String fullName, String email,
                    String phone, String password, Role role) {
            super(id, fullName, phone);
            this.email = email;
            this.password = password;
            this.role = role;
            this.status = AccountStatus.ACTIVE;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public Role getRole() { return role; }
        public AccountStatus getStatus() { return status; }
        public void setRole(Role role) { this.role = role; }
        public void setStatus(AccountStatus status) { this.status = status; }

        @Override public String getPersonType() { return "USER - " + role; }
    }

    // ============================================================
    // RESIDENT CLASS
    // ============================================================
    static class Resident extends Person {
        private static final long serialVersionUID = 1L;
        private long userId;
        private String email;
        private String collegeName;
        private String emergencyContact;
        private LocalDate registrationDate;

        public Resident(long id, long userId, String fullName,
                        String email, String phone, String collegeName,
                        String emergencyContact) {
            super(id, fullName, phone);
            this.userId = userId;
            this.email = email;
            this.collegeName = collegeName;
            this.emergencyContact = emergencyContact;
            this.registrationDate = LocalDate.now();
        }

        public long getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getCollegeName() { return collegeName; }
        public String getEmergencyContact() { return emergencyContact; }
        public LocalDate getRegistrationDate() { return registrationDate; }

        @Override public String getPersonType() { return "RESIDENT"; }
    }

    // ============================================================
    // ROOM CLASS
    // ============================================================
    static class Room implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private String roomNumber;
        private int floorNumber;
        private String roomType;
        private double monthlyFee;
        private RoomStatus status;

        public Room(long id, String roomNumber, int floorNumber,
                    String roomType, double monthlyFee) {
            this.id = id;
            this.roomNumber = roomNumber;
            this.floorNumber = floorNumber;
            this.roomType = roomType;
            this.monthlyFee = monthlyFee;
            this.status = RoomStatus.AVAILABLE;
        }

        @Override public long getId() { return id; }
        public String getRoomNumber() { return roomNumber; }
        public int getFloorNumber() { return floorNumber; }
        public String getRoomType() { return roomType; }
        public double getMonthlyFee() { return monthlyFee; }
        public RoomStatus getStatus() { return status; }
        public void setStatus(RoomStatus s) { this.status = s; }
    }

    // ============================================================
    // BED CLASS
    // ============================================================
    static class Bed implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long roomId;
        private String bedNumber;
        private BedStatus status;

        public Bed(long id, long roomId, String bedNumber) {
            this.id = id;
            this.roomId = roomId;
            this.bedNumber = bedNumber;
            this.status = BedStatus.AVAILABLE;
        }

        @Override public long getId() { return id; }
        public long getRoomId() { return roomId; }
        public String getBedNumber() { return bedNumber; }
        public BedStatus getStatus() { return status; }
        public void setStatus(BedStatus s) { this.status = s; }
    }

    // ============================================================
    // ROOM REQUEST
    // ============================================================
    static class RoomRequest implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long residentId;
        private String preferredRoomType;
        private LocalDate requestDate;
        private RequestStatus status;
        private Long reviewedBy;
        private String remarks;

        public RoomRequest(long id, long residentId, String preferredRoomType) {
            this.id = id;
            this.residentId = residentId;
            this.preferredRoomType = preferredRoomType;
            this.requestDate = LocalDate.now();
            this.status = RequestStatus.PENDING;
        }

        @Override public long getId() { return id; }
        public long getResidentId() { return residentId; }
        public String getPreferredRoomType() { return preferredRoomType; }
        public LocalDate getRequestDate() { return requestDate; }
        public RequestStatus getStatus() { return status; }
        public Long getReviewedBy() { return reviewedBy; }
        public String getRemarks() { return remarks; }

        public void review(RequestStatus s, long staffId, String remarks)
                throws ValidationException {
            if (this.status != RequestStatus.PENDING)
                throw new ValidationException("Only pending requests can be reviewed.");
            this.status = s;
            this.reviewedBy = staffId;
            this.remarks = remarks;
        }
    }

    // ============================================================
    // ALLOCATION
    // ============================================================
    static class Allocation implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long residentId;
        private long bedId;
        private LocalDate allocationDate;
        private LocalDate vacatingDate;
        private AllocationStatus status;
        private long createdBy;

        public Allocation(long id, long residentId, long bedId,
                          LocalDate allocationDate, long createdBy) {
            this.id = id;
            this.residentId = residentId;
            this.bedId = bedId;
            this.allocationDate = allocationDate;
            this.createdBy = createdBy;
            this.status = AllocationStatus.ACTIVE;
        }

        @Override public long getId() { return id; }
        public long getResidentId() { return residentId; }
        public long getBedId() { return bedId; }
        public LocalDate getAllocationDate() { return allocationDate; }
        public LocalDate getVacatingDate() { return vacatingDate; }
        public AllocationStatus getStatus() { return status; }
        public long getCreatedBy() { return createdBy; }

        public void vacate(LocalDate date) throws ValidationException {
            if (status == AllocationStatus.VACATED)
                throw new ValidationException("Already vacated.");
            if (date.isBefore(allocationDate))
                throw new ValidationException("Vacating date cannot be before allocation date.");
            this.vacatingDate = date;
            this.status = AllocationStatus.VACATED;
        }
    }

    // ============================================================
    // BILL
    // ============================================================
    static class Bill implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long residentId;
        private YearMonth billingMonth;
        private double totalAmount;
        private LocalDate dueDate;
        private BillStatus status;

        public Bill(long id, long residentId, YearMonth billingMonth,
                    double totalAmount, LocalDate dueDate) {
            this.id = id;
            this.residentId = residentId;
            this.billingMonth = billingMonth;
            this.totalAmount = totalAmount;
            this.dueDate = dueDate;
            this.status = BillStatus.UNPAID;
        }

        @Override public long getId() { return id; }
        public long getResidentId() { return residentId; }
        public YearMonth getBillingMonth() { return billingMonth; }
        public double getTotalAmount() { return totalAmount; }
        public LocalDate getDueDate() { return dueDate; }
        public BillStatus getStatus() { return status; }

        public void updateStatus(double totalPaid) {
            if (totalPaid <= 0) status = BillStatus.UNPAID;
            else if (totalPaid < totalAmount) status = BillStatus.PARTIALLY_PAID;
            else status = BillStatus.PAID;
        }
    }

    // ============================================================
    // PAYMENT
    // ============================================================
    static class Payment implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long billId;
        private double amount;
        private LocalDate paymentDate;
        private PaymentMethod method;
        private String transactionReference;
        private PaymentStatus status;
        private long recordedBy;

        public Payment(long id, long billId, double amount,
                       PaymentMethod method, String transactionReference,
                       long recordedBy) {
            this.id = id;
            this.billId = billId;
            this.amount = amount;
            this.method = method;
            this.transactionReference = transactionReference;
            this.recordedBy = recordedBy;
            this.paymentDate = LocalDate.now();
            this.status = PaymentStatus.SUCCESS;
        }

        @Override public long getId() { return id; }
        public long getBillId() { return billId; }
        public double getAmount() { return amount; }
        public LocalDate getPaymentDate() { return paymentDate; }
        public PaymentMethod getMethod() { return method; }
        public String getTransactionReference() { return transactionReference; }
        public PaymentStatus getStatus() { return status; }
        public long getRecordedBy() { return recordedBy; }
    }

    // ============================================================
    // AUDIT LOG
    // ============================================================
    static class AuditLog implements Serializable, Identifiable {
        private static final long serialVersionUID = 1L;
        private long id;
        private long userId;
        private String action;
        private String recordType;
        private long recordId;
        private LocalDate date;
        private String details;

        public AuditLog(long id, long userId, String action,
                        String recordType, long recordId, String details) {
            this.id = id;
            this.userId = userId;
            this.action = action;
            this.recordType = recordType;
            this.recordId = recordId;
            this.details = details;
            this.date = LocalDate.now();
        }

        @Override public long getId() { return id; }
        public long getUserId() { return userId; }
        public String getAction() { return action; }
        public String getRecordType() { return recordType; }
        public long getRecordId() { return recordId; }
        public LocalDate getDate() { return date; }
        public String getDetails() { return details; }
    }

    // ============================================================
    // DATA STORE
    // ============================================================
    static class DataStore implements Serializable {
        private static final long serialVersionUID = 1L;
        Map<Long, User> users = new LinkedHashMap<>();
        Map<Long, Resident> residents = new LinkedHashMap<>();
        Map<Long, Room> rooms = new LinkedHashMap<>();
        Map<Long, Bed> beds = new LinkedHashMap<>();
        Map<Long, RoomRequest> requests = new LinkedHashMap<>();
        Map<Long, Allocation> allocations = new LinkedHashMap<>();
        Map<Long, Bill> bills = new LinkedHashMap<>();
        Map<Long, Payment> payments = new LinkedHashMap<>();
        Map<Long, AuditLog> auditLogs = new LinkedHashMap<>();

        long nextUserId = 1, nextResidentId = 1, nextRoomId = 1;
        long nextBedId = 1, nextRequestId = 1, nextAllocationId = 1;
        long nextBillId = 1, nextPaymentId = 1, nextAuditId = 1;
    }

    // ============================================================
    // GENERIC REPOSITORY
    // ============================================================
    interface Repository<T extends Identifiable> {
        T save(T object);
        Optional<T> findById(long id);
        List<T> findAll();
        void delete(long id);
    }

    static class MapRepository<T extends Identifiable> implements Repository<T> {
        private final Map<Long, T> data;
        public MapRepository(Map<Long, T> data) { this.data = data; }

        @Override public T save(T object) { data.put(object.getId(), object); return object; }
        @Override public Optional<T> findById(long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<T> findAll() { return new ArrayList<>(data.values()); }
        @Override public void delete(long id) { data.remove(id); }
    }

    // ============================================================
    // SERVICE CLASS
    // ============================================================
    static class HostelService {
        private final DataStore db;
        private final Repository<User> userRepo;
        private final Repository<Resident> residentRepo;
        private final Repository<Room> roomRepo;
        private final Repository<Bed> bedRepo;
        private final Repository<RoomRequest> requestRepo;
        private final Repository<Allocation> allocationRepo;
        private final Repository<Bill> billRepo;
        private final Repository<Payment> paymentRepo;
        private final Repository<AuditLog> auditRepo;

        public HostelService(DataStore db) {
            this.db = db;
            userRepo = new MapRepository<>(db.users);
            residentRepo = new MapRepository<>(db.residents);
            roomRepo = new MapRepository<>(db.rooms);
            bedRepo = new MapRepository<>(db.beds);
            requestRepo = new MapRepository<>(db.requests);
            allocationRepo = new MapRepository<>(db.allocations);
            billRepo = new MapRepository<>(db.bills);
            paymentRepo = new MapRepository<>(db.payments);
            auditRepo = new MapRepository<>(db.auditLogs);
        }

        // User
        public User createUser(String name, String email, String phone,
                               String password, Role role) throws HostelException {
            if (name == null || name.isBlank()) throw new ValidationException("Name is required.");
            if (email == null || !email.contains("@")) throw new ValidationException("Valid email is required.");
            if (password == null || password.length() < 4)
                throw new ValidationException("Password must be at least 4 characters.");
            boolean exists = db.users.values().stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
            if (exists) throw new ConflictException("Email already exists.");
            User user = new User(db.nextUserId++, name, email, phone, password, role);
            userRepo.save(user);
            return user;
        }

        public User login(String email, String password) throws ValidationException {
            User user = db.users.values().stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                    .findFirst().orElse(null);
            if (user == null) throw new ValidationException("Invalid email or password.");
            if (user.getStatus() == AccountStatus.INACTIVE) throw new ValidationException("Account is inactive.");
            return user;
        }

        // Resident
        public Resident registerResident(String name, String email, String phone,
                                         String college, String emergencyContact,
                                         String password) throws HostelException {
            User user = createUser(name, email, phone, password, Role.RESIDENT);
            Resident r = new Resident(db.nextResidentId++, user.getId(), name, email, phone, college, emergencyContact);
            residentRepo.save(r);
            return r;
        }

        public Resident getResident(long id) throws NotFoundException {
            return residentRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Resident not found."));
        }

        // Room
        public Room addRoom(String roomNumber, int floor, String type, double fee) throws HostelException {
            if (roomNumber == null || roomNumber.isBlank()) throw new ValidationException("Room number is required.");
            if (floor < 0) throw new ValidationException("Floor cannot be negative.");
            if (fee < 0) throw new ValidationException("Fee cannot be negative.");
            boolean exists = db.rooms.values().stream()
                    .anyMatch(r -> r.getRoomNumber().equalsIgnoreCase(roomNumber));
            if (exists) throw new ConflictException("Room number already exists.");
            Room room = new Room(db.nextRoomId++, roomNumber, floor, type, fee);
            roomRepo.save(room);
            return room;
        }

        public void setRoomMaintenance(long roomId, boolean maintenance) throws NotFoundException {
            Room room = roomRepo.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("Room not found."));
            room.setStatus(maintenance ? RoomStatus.MAINTENANCE : RoomStatus.AVAILABLE);
        }

        // Bed
        public Bed addBed(long roomId, String bedNumber) throws HostelException {
            Room room = roomRepo.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("Room not found."));
            if (room.getStatus() == RoomStatus.MAINTENANCE)
                throw new ConflictException("Cannot add beds to maintenance room.");
            boolean exists = db.beds.values().stream()
                    .anyMatch(b -> b.getRoomId() == roomId && b.getBedNumber().equalsIgnoreCase(bedNumber));
            if (exists) throw new ConflictException("Bed number already exists in this room.");
            Bed bed = new Bed(db.nextBedId++, roomId, bedNumber);
            bedRepo.save(bed);
            return bed;
        }

        public void setBedMaintenance(long bedId, boolean maintenance) throws HostelException {
            Bed bed = bedRepo.findById(bedId)
                    .orElseThrow(() -> new NotFoundException("Bed not found."));
            if (isBedOccupied(bedId) && maintenance)
                throw new ConflictException("Occupied bed cannot be set to maintenance.");
            bed.setStatus(maintenance ? BedStatus.MAINTENANCE : BedStatus.AVAILABLE);
        }

        public boolean isBedOccupied(long bedId) {
            return db.allocations.values().stream()
                    .anyMatch(a -> a.getBedId() == bedId && a.getStatus() == AllocationStatus.ACTIVE);
        }

        // Request
        public RoomRequest createRoomRequest(long residentId, String preferredType) throws HostelException {
            getResident(residentId);
            boolean pending = db.requests.values().stream()
                    .anyMatch(r -> r.getResidentId() == residentId && r.getStatus() == RequestStatus.PENDING);
            if (pending) throw new ConflictException("Already has a pending request.");
            RoomRequest req = new RoomRequest(db.nextRequestId++, residentId, preferredType);
            requestRepo.save(req);
            return req;
        }

        public void reviewRequest(long requestId, long staffId, RequestStatus status, String remarks)
                throws HostelException {
            RoomRequest req = requestRepo.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Request not found."));
            req.review(status, staffId, remarks);
        }

        // Allocation
        public Allocation allocateBed(long residentId, long bedId, long staffId) throws HostelException {
            getResident(residentId);
            Bed bed = bedRepo.findById(bedId)
                    .orElseThrow(() -> new NotFoundException("Bed not found."));
            if (bed.getStatus() == BedStatus.MAINTENANCE) throw new ConflictException("Bed is under maintenance.");
            if (isBedOccupied(bedId)) throw new ConflictException("Bed is already allocated.");
            boolean already = db.allocations.values().stream()
                    .anyMatch(a -> a.getResidentId() == residentId && a.getStatus() == AllocationStatus.ACTIVE);
            if (already) throw new ConflictException("Resident already has an active allocation.");
            Allocation alloc = new Allocation(db.nextAllocationId++, residentId, bedId, LocalDate.now(), staffId);
            allocationRepo.save(alloc);
            return alloc;
        }

        public void checkout(long allocationId) throws HostelException {
            Allocation alloc = allocationRepo.findById(allocationId)
                    .orElseThrow(() -> new NotFoundException("Allocation not found."));
            alloc.vacate(LocalDate.now());
        }

        // Billing
        public Bill createBill(long residentId, YearMonth month, double amount) throws HostelException {
            getResident(residentId);
            if (amount < 0) throw new ValidationException("Amount cannot be negative.");
            boolean dup = db.bills.values().stream()
                    .anyMatch(b -> b.getResidentId() == residentId && b.getBillingMonth().equals(month));
            if (dup) throw new ConflictException("Bill already exists for this month.");
            Bill bill = new Bill(db.nextBillId++, residentId, month, amount, month.atEndOfMonth());
            billRepo.save(bill);
            return bill;
        }

        public Payment recordPayment(long billId, double amount, PaymentMethod method,
                                      String reference, long staffId) throws HostelException {
            Bill bill = billRepo.findById(billId)
                    .orElseThrow(() -> new NotFoundException("Bill not found."));
            if (amount <= 0) throw new ValidationException("Payment must be greater than zero.");
            double alreadyPaid = getPaidAmount(billId);
            if (alreadyPaid + amount > bill.getTotalAmount())
                throw new ConflictException("Payment exceeds outstanding amount.");
            if (reference != null && !reference.isBlank()) {
                boolean dupRef = db.payments.values().stream()
                        .anyMatch(p -> reference.equalsIgnoreCase(p.getTransactionReference()));
                if (dupRef) throw new ConflictException("Transaction reference already exists.");
            }
            Payment payment = new Payment(db.nextPaymentId++, billId, amount, method, reference, staffId);
            paymentRepo.save(payment);
            bill.updateStatus(alreadyPaid + amount);
            return payment;
        }

        public double getPaidAmount(long billId) {
            return db.payments.values().stream()
                    .filter(p -> p.getBillId() == billId && p.getStatus() == PaymentStatus.SUCCESS)
                    .mapToDouble(Payment::getAmount).sum();
        }

        public double getOutstandingAmount(long billId) {
            Bill bill = db.bills.get(billId);
            if (bill == null) return 0;
            return Math.max(0, bill.getTotalAmount() - getPaidAmount(billId));
        }

        // Audit
        public void audit(long userId, String action, String recordType, long recordId, String details) {
            AuditLog log = new AuditLog(db.nextAuditId++, userId, action, recordType, recordId, details);
            auditRepo.save(log);
        }

        // Stats
        public int totalBeds() { return db.beds.size(); }
        public int occupiedBeds() {
            return (int) db.allocations.values().stream()
                    .filter(a -> a.getStatus() == AllocationStatus.ACTIVE).count();
        }
        public int maintenanceBeds() {
            return (int) db.beds.values().stream()
                    .filter(b -> b.getStatus() == BedStatus.MAINTENANCE).count();
        }
        public int availableBeds() { return totalBeds() - occupiedBeds() - maintenanceBeds(); }
        public double totalOutstanding() {
            return db.bills.values().stream()
                    .mapToDouble(b -> getOutstandingAmount(b.getId())).sum();
        }

        // Lookups
        public List<Bed> getAvailableBeds() {
            return db.beds.values().stream()
                    .filter(b -> b.getStatus() == BedStatus.AVAILABLE && !isBedOccupied(b.getId()))
                    .collect(Collectors.toList());
        }

        public List<Bill> getResidentBills(long residentId) {
            return db.bills.values().stream()
                    .filter(b -> b.getResidentId() == residentId)
                    .collect(Collectors.toList());
        }

        public List<Payment> getResidentPayments(long residentId) {
            Set<Long> billIds = db.bills.values().stream()
                    .filter(b -> b.getResidentId() == residentId)
                    .map(Bill::getId).collect(Collectors.toSet());
            return db.payments.values().stream()
                    .filter(p -> billIds.contains(p.getBillId()))
                    .collect(Collectors.toList());
        }

        public Allocation getActiveAllocation(long residentId) {
            return db.allocations.values().stream()
                    .filter(a -> a.getResidentId() == residentId && a.getStatus() == AllocationStatus.ACTIVE)
                    .findFirst().orElse(null);
        }

        public String getBedLocation(long bedId) {
            Bed bed = db.beds.get(bedId);
            if (bed == null) return "Unknown";
            Room room = db.rooms.get(bed.getRoomId());
            if (room == null) return "Unknown";
            return "Room " + room.getRoomNumber() + " / Bed " + bed.getBedNumber();
        }

        public Resident getResidentByUserId(long userId) {
            return db.residents.values().stream()
                    .filter(r -> r.getUserId() == userId).findFirst().orElse(null);
        }

        public DataStore getDb() { return db; }
    }

    // ============================================================
    // FILE DATABASE
    // ============================================================
    static class FileDatabase {
        private static final String FILE_NAME = "hostel_data.dat";
        public static void save(DataStore data) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                out.writeObject(data);
            } catch (IOException e) {
                System.err.println("WARNING: Could not save data: " + e.getMessage());
            }
        }
        public static DataStore load() {
            File file = new File(FILE_NAME);
            if (!file.exists()) return new DataStore();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                return (DataStore) in.readObject();
            } catch (Exception e) {
                System.err.println("WARNING: Starting new database.");
                return new DataStore();
            }
        }
    }

    // ============================================================
    // CUSTOM UI COMPONENTS
    // ============================================================

    // Rounded Panel
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private boolean hasShadow;

        public RoundedPanel(int radius, Color bgColor) {
            this(radius, bgColor, false);
        }

        public RoundedPanel(int radius, Color bgColor, boolean hasShadow) {
            super();
            this.radius = radius;
            this.bgColor = bgColor;
            this.hasShadow = hasShadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (hasShadow) {
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 2, getHeight() - 2, radius, radius));
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fill(new RoundRectangle2D.Float(1, 1, getWidth() - 1, getHeight() - 1, radius, radius));
            }

            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - (hasShadow ? 3 : 0),
                    getHeight() - (hasShadow ? 3 : 0), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Modern Button
    static class ModernButton extends JButton {
        private Color bgColor;
        private Color hoverColor;
        private Color pressColor;
        private Color textColor;
        private boolean isHovered = false;
        private boolean isPressed = false;
        private int radius = 10;

        public ModernButton(String text, Color bg, Color textColor) {
            super(text);
            this.bgColor = bg;
            this.textColor = textColor;
            this.hoverColor = bg.brighter();
            this.pressColor = bg.darker();

            setFont(Theme.FONT_BUTTON);
            setForeground(textColor);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 42));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { isHovered = false; isPressed = false; repaint(); }
                @Override public void mousePressed(MouseEvent e) { isPressed = true; repaint(); }
                @Override public void mouseReleased(MouseEvent e) { isPressed = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = isPressed ? pressColor : (isHovered ? hoverColor : bgColor);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Modern Text Field
    static class ModernTextField extends JTextField {
        private String placeholder;
        private boolean showingPlaceholder = true;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(Theme.FONT_INPUT);
            setForeground(Theme.TEXT_PRIMARY);
            setCaretColor(Theme.PRIMARY);
            setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, Theme.BORDER),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)));
            setPreferredSize(new Dimension(300, 42));
            setBackground(Theme.BG_INPUT);

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            new RoundedBorder(8, Theme.PRIMARY),
                            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
                }
                @Override
                public void focusLost(FocusEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            new RoundedBorder(8, Theme.BORDER),
                            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.TEXT_LIGHT);
                g2.setFont(Theme.FONT_INPUT);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, 14, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    // Modern Password Field
    static class ModernPasswordField extends JPasswordField {
        private String placeholder;

        public ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setFont(Theme.FONT_INPUT);
            setForeground(Theme.TEXT_PRIMARY);
            setCaretColor(Theme.PRIMARY);
            setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, Theme.BORDER),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)));
            setPreferredSize(new Dimension(300, 42));
            setBackground(Theme.BG_INPUT);

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            new RoundedBorder(8, Theme.PRIMARY),
                            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
                }
                @Override
                public void focusLost(FocusEvent e) {
                    setBorder(BorderFactory.createCompoundBorder(
                            new RoundedBorder(8, Theme.BORDER),
                            BorderFactory.createEmptyBorder(8, 14, 8, 14)));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.TEXT_LIGHT);
                g2.setFont(Theme.FONT_INPUT);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, 14, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    // Rounded Border
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x, y, w - 1, h - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    // Sidebar Button
    static class SidebarButton extends JPanel {
        private String text;
        private String icon;
        private boolean isSelected = false;
        private boolean isHovered = false;
        private Runnable action;

        public SidebarButton(String icon, String text, Runnable action) {
            this.icon = icon;
            this.text = text;
            this.action = action;
            setOpaque(false);
            setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH - 20, 44));
            setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH - 20, 44));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (action != null) action.run();
                }
            });
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (isSelected) {
                g2.setColor(Theme.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            } else if (isHovered) {
                g2.setColor(Theme.BG_SIDEBAR_HOVER);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            }

            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            g2.setColor(isSelected ? Theme.TEXT_WHITE : Theme.TEXT_SIDEBAR);
            g2.drawString(icon, 16, 29);

            g2.setFont(Theme.FONT_SIDEBAR);
            g2.setColor(isSelected ? Theme.TEXT_WHITE : Theme.TEXT_SIDEBAR);
            g2.drawString(text, 48, 28);

            g2.dispose();
        }
    }

    // Stat Card
    static class StatCard extends RoundedPanel {
        public StatCard(String title, String value, String subtitle, Color accentColor) {
            super(Theme.BORDER_RADIUS, Theme.BG_CARD, true);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
            setPreferredSize(new Dimension(220, 140));

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(Theme.FONT_SMALL_BOLD);
            titleLabel.setForeground(Theme.TEXT_SECONDARY);
            topPanel.add(titleLabel, BorderLayout.WEST);

            // Accent dot
            JPanel dot = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(accentColor);
                    g2.fillOval(0, 4, 10, 10);
                    g2.dispose();
                }
            };
            dot.setOpaque(false);
            dot.setPreferredSize(new Dimension(14, 18));
            topPanel.add(dot, BorderLayout.EAST);

            add(topPanel, BorderLayout.NORTH);

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(Theme.FONT_STAT_VALUE);
            valueLabel.setForeground(Theme.TEXT_PRIMARY);
            add(valueLabel, BorderLayout.CENTER);

            JLabel subLabel = new JLabel(subtitle);
            subLabel.setFont(Theme.FONT_SMALL);
            subLabel.setForeground(Theme.TEXT_SECONDARY);
            add(subLabel, BorderLayout.SOUTH);
        }
    }

    // Modern Table
    static JScrollPane createModernTable(String[] columns, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(Theme.FONT_TABLE_CELL);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.PRIMARY_BG);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setBackground(Theme.BG_CARD);
        table.setFillsViewportHeight(true);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_TABLE_HEADER);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setBackground(Theme.BG_CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.DIVIDER));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 48));
        header.setReorderingAllowed(false);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Theme.BG_CARD : new Color(249, 250, 251));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.BG_CARD);

        // Modern scrollbar
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(200, 200, 200);
                trackColor = Theme.BG_CARD;
            }
            @Override protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        return scrollPane;
    }

    // ============================================================
    // NOTIFICATION TOAST
    // ============================================================
    // ============================================================
// NOTIFICATION TOAST
// ============================================================
static void showToast(JFrame parent, String message, boolean isSuccess) {
    JDialog toast = new JDialog(parent);
    toast.setUndecorated(true);
    toast.setSize(380, 56);

    // Position at top center of the main window
    int x = parent.getX() + (parent.getWidth() - 380) / 2;
    int y = parent.getY() + 30;
    toast.setLocation(x, y);

    JPanel panel = new RoundedPanel(12, isSuccess ? new Color(220, 252, 231) : new Color(254, 226, 226)) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Icon
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            g2.setColor(isSuccess ? Theme.SUCCESS : Theme.DANGER);
            g2.drawString(isSuccess ? "✓" : "✕", 18, 35);

            // Text
            g2.setFont(Theme.FONT_BODY_BOLD);
            g2.setColor(isSuccess ? new Color(22, 101, 52) : new Color(153, 27, 27));
            g2.drawString(message, 50, 34);

            g2.dispose();
        }
    };

    panel.setBorder(BorderFactory.createLineBorder(
            isSuccess ? new Color(187, 247, 208) : new Color(254, 202, 202), 1));

    toast.setContentPane(panel);
    toast.setVisible(true);

    // FIX: Use javax.swing.Timer explicitly to avoid ambiguity with java.util.Timer
    javax.swing.Timer timer = new javax.swing.Timer(2500, e -> toast.dispose());
    timer.setRepeats(false);
    timer.start();
}
    // ============================================================
    // MAIN APPLICATION FRAME
    // ============================================================
    static class MainApp extends JFrame {
        private DataStore database;
        private HostelService service;
        private User currentUser;

        private JPanel sidebarPanel;
        private JPanel contentPanel;
        private CardLayout contentLayout;
        private List<SidebarButton> sidebarButtons = new ArrayList<>();

        public MainApp() {
            database = FileDatabase.load();
            service = new HostelService(database);
            seedAdminIfNeeded();

            setTitle("Hostel Management System");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1400, 850);
            setMinimumSize(new Dimension(1100, 700));
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            showLoginScreen();
            setVisible(true);
        }

        private void seedAdminIfNeeded() {
            if (database.users.isEmpty()) {
                try {
                    User admin = service.createUser("System Admin", "admin@hostel.com",
                            "9999999999", "admin123", Role.ADMIN);
                    service.audit(admin.getId(), "CREATE_ADMIN", "USER", admin.getId(),
                            "Default administrator created.");
                    FileDatabase.save(database);
                } catch (Exception e) {
                    System.err.println("Could not create default admin.");
                }
            }
        }

        // ============================================================
        // LOGIN SCREEN
        // ============================================================
        private void showLoginScreen() {
            getContentPane().removeAll();

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(238, 242, 255));

            // Background gradient painting
            JPanel bgPanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    GradientPaint gp = new GradientPaint(0, 0, new Color(238, 242, 255),
                            getWidth(), getHeight(), new Color(224, 231, 255));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };

            // Login Card
            RoundedPanel card = new RoundedPanel(20, Theme.BG_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(48, 48, 48, 48));
            card.setPreferredSize(new Dimension(440, 580));

            // Logo / Title area
            JLabel logo = new JLabel("🏨");
            logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(logo);
            card.add(Box.createVerticalStrut(8));

            JLabel title = new JLabel("Welcome Back");
            title.setFont(Theme.FONT_TITLE);
            title.setForeground(Theme.TEXT_PRIMARY);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(title);

            JLabel subtitle = new JLabel("Sign in to Hostel Management System");
            subtitle.setFont(Theme.FONT_BODY);
            subtitle.setForeground(Theme.TEXT_SECONDARY);
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(subtitle);
            card.add(Box.createVerticalStrut(32));

            // Email
            JLabel emailLabel = createFieldLabel("Email Address");
            card.add(emailLabel);
            card.add(Box.createVerticalStrut(6));

            ModernTextField emailField = new ModernTextField("Enter your email");
            emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(emailField);
            card.add(Box.createVerticalStrut(16));

            // Password
            JLabel passLabel = createFieldLabel("Password");
            card.add(passLabel);
            card.add(Box.createVerticalStrut(6));

            ModernPasswordField passField = new ModernPasswordField("Enter your password");
            passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            passField.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(passField);
            card.add(Box.createVerticalStrut(24));

            // Login Button
            ModernButton loginBtn = new ModernButton("Sign In", Theme.PRIMARY, Theme.TEXT_WHITE);
            loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(loginBtn);
            card.add(Box.createVerticalStrut(16));

            // Divider
            JSeparator sep = new JSeparator();
            sep.setForeground(Theme.DIVIDER);
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            card.add(sep);
            card.add(Box.createVerticalStrut(16));

            // Register Button
            ModernButton registerBtn = new ModernButton("Create New Account", new Color(241, 245, 249), Theme.PRIMARY);
            registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(registerBtn);
            card.add(Box.createVerticalStrut(12));

            // Default credentials info
            JLabel infoLabel = new JLabel("<html><center>Default Admin: admin@hostel.com / admin123</center></html>");
            infoLabel.setFont(Theme.FONT_SMALL);
            infoLabel.setForeground(Theme.TEXT_LIGHT);
            infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(infoLabel);

            // Actions
            loginBtn.addActionListener(e -> {
                try {
                    currentUser = service.login(emailField.getText(), new String(passField.getPassword()));
                    showMainApplication();
                } catch (HostelException ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });

            // Enter key support
            passField.addActionListener(e -> loginBtn.doClick());
            emailField.addActionListener(e -> passField.requestFocus());

            registerBtn.addActionListener(e -> showRegistrationScreen());

            bgPanel.add(card);
            getContentPane().add(bgPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }

        // ============================================================
        // REGISTRATION SCREEN
        // ============================================================
        private void showRegistrationScreen() {
            getContentPane().removeAll();

            JPanel bgPanel = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    GradientPaint gp = new GradientPaint(0, 0, new Color(238, 242, 255),
                            getWidth(), getHeight(), new Color(224, 231, 255));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };

            RoundedPanel card = new RoundedPanel(20, Theme.BG_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(36, 48, 36, 48));
            card.setPreferredSize(new Dimension(480, 680));

            JLabel title = new JLabel("Create Account");
            title.setFont(Theme.FONT_TITLE);
            title.setForeground(Theme.TEXT_PRIMARY);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(title);

            JLabel subtitle = new JLabel("Register as a new resident");
            subtitle.setFont(Theme.FONT_BODY);
            subtitle.setForeground(Theme.TEXT_SECONDARY);
            subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(subtitle);
            card.add(Box.createVerticalStrut(24));

            ModernTextField nameField = addFormField(card, "Full Name", "Enter your full name");
            ModernTextField emailField = addFormField(card, "Email", "Enter your email");
            ModernTextField phoneField = addFormField(card, "Phone", "Enter phone number");
            ModernTextField collegeField = addFormField(card, "College Name", "Enter college name");
            ModernTextField emergencyField = addFormField(card, "Emergency Contact", "Emergency contact number");

            JLabel passLabel = createFieldLabel("Password");
            card.add(passLabel);
            card.add(Box.createVerticalStrut(4));
            ModernPasswordField passField = new ModernPasswordField("Create a password (min 4 chars)");
            passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            passField.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(passField);
            card.add(Box.createVerticalStrut(20));

            ModernButton registerBtn = new ModernButton("Register", Theme.SECONDARY, Theme.TEXT_WHITE);
            registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(registerBtn);
            card.add(Box.createVerticalStrut(12));

            ModernButton backBtn = new ModernButton("← Back to Login", new Color(241, 245, 249), Theme.TEXT_SECONDARY);
            backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(backBtn);

            registerBtn.addActionListener(e -> {
                try {
                    service.registerResident(nameField.getText(), emailField.getText(),
                            phoneField.getText(), collegeField.getText(), emergencyField.getText(),
                            new String(passField.getPassword()));
                    FileDatabase.save(database);
                    showToast(this, "Registration successful! Please login.", true);
                    showLoginScreen();
                } catch (HostelException ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });

            backBtn.addActionListener(e -> showLoginScreen());

            bgPanel.add(card);
            getContentPane().add(bgPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }

        // ============================================================
        // MAIN APPLICATION
        // ============================================================
        private void showMainApplication() {
            getContentPane().removeAll();

            JPanel mainWrapper = new JPanel(new BorderLayout());

            // Sidebar
            sidebarPanel = createSidebar();
            mainWrapper.add(sidebarPanel, BorderLayout.WEST);

            // Content area
            contentPanel = new JPanel();
            contentLayout = new CardLayout();
            contentPanel.setLayout(contentLayout);
            contentPanel.setBackground(Theme.BG_MAIN);
            mainWrapper.add(contentPanel, BorderLayout.CENTER);

            // Build pages based on role
            buildPages();

            getContentPane().add(mainWrapper, BorderLayout.CENTER);
            revalidate();
            repaint();
        }

        private JPanel createSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 0));
            sidebar.setBackground(Theme.BG_SIDEBAR);
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

            // Logo section
            JPanel logoPanel = new JPanel();
            logoPanel.setOpaque(false);
            logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
            logoPanel.setBorder(BorderFactory.createEmptyBorder(24, 10, 24, 10));
            logoPanel.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, 100));

            JLabel logoIcon = new JLabel("🏨 HMS");
            logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 22));
            logoIcon.setForeground(Theme.TEXT_WHITE);
            logoIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
            logoPanel.add(logoIcon);

            JLabel roleLabel = new JLabel(currentUser.getRole().toString());
            roleLabel.setFont(Theme.FONT_SMALL_BOLD);
            roleLabel.setForeground(Theme.PRIMARY_LIGHT);
            roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            logoPanel.add(roleLabel);

            sidebar.add(logoPanel);

            // Divider
            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(55, 48, 100));
            sep.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH - 20, 1));
            sidebar.add(sep);
            sidebar.add(Box.createVerticalStrut(16));

            return sidebar;
        }

        private void addSidebarSection(String title) {
            JLabel sectionLabel = new JLabel(title.toUpperCase());
            sectionLabel.setFont(Theme.FONT_SIDEBAR_HEADER);
            sectionLabel.setForeground(new Color(129, 120, 248));
            sectionLabel.setBorder(BorderFactory.createEmptyBorder(12, 16, 6, 0));
            sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            sectionLabel.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, 30));
            sidebarPanel.add(sectionLabel);
        }

        private SidebarButton addSidebarButton(String icon, String text, String pageKey, Runnable action) {
            SidebarButton btn = new SidebarButton(icon, text, () -> {
                sidebarButtons.forEach(b -> b.setSelected(false));
                action.run();
            });
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebarButtons.add(btn);
            sidebarPanel.add(btn);
            sidebarPanel.add(Box.createVerticalStrut(4));
            return btn;
        }

        // ============================================================
        // BUILD PAGES BY ROLE
        // ============================================================
        private void buildPages() {
            sidebarButtons.clear();

            switch (currentUser.getRole()) {
                case ADMIN -> buildAdminPages();
                case RECEPTIONIST -> buildReceptionistPages();
                case RESIDENT -> buildResidentPages();
            }

            // Logout at bottom
            sidebarPanel.add(Box.createVerticalGlue());
            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(55, 48, 100));
            sep.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH - 20, 1));
            sidebarPanel.add(sep);
            sidebarPanel.add(Box.createVerticalStrut(8));

            // User info
            JPanel userPanel = new JPanel();
            userPanel.setOpaque(false);
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
            userPanel.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
            userPanel.setMaximumSize(new Dimension(Theme.SIDEBAR_WIDTH, 50));

            JLabel userName = new JLabel(currentUser.getFullName());
            userName.setFont(Theme.FONT_SMALL_BOLD);
            userName.setForeground(Theme.TEXT_WHITE);
            userPanel.add(userName);

            JLabel userEmail = new JLabel(currentUser.getEmail());
            userEmail.setFont(Theme.FONT_SMALL);
            userEmail.setForeground(Theme.TEXT_SIDEBAR);
            userPanel.add(userEmail);

            sidebarPanel.add(userPanel);
            sidebarPanel.add(Box.createVerticalStrut(8));

            addSidebarButton("🚪", "Logout", "logout", () -> {
                currentUser = null;
                FileDatabase.save(database);
                showLoginScreen();
            });

            // Show first page
            if (!sidebarButtons.isEmpty()) {
                sidebarButtons.get(0).setSelected(true);
            }
        }

        // ============================================================
        // ADMIN PAGES
        // ============================================================
        private void buildAdminPages() {
            addSidebarSection("Overview");
            SidebarButton dashBtn = addSidebarButton("📊", "Dashboard", "dashboard", () -> {
                showPage("dashboard");
            });
            contentPanel.add(createDashboardPage(), "dashboard");
            dashBtn.setSelected(true);

            addSidebarSection("Management");
            addSidebarButton("🏠", "Rooms", "rooms", () -> {
                contentPanel.add(createRoomsPage(), "rooms");
                showPage("rooms");
            });
            addSidebarButton("🛏️", "Beds", "beds", () -> {
                contentPanel.add(createBedsPage(), "beds");
                showPage("beds");
            });
            addSidebarButton("👥", "Residents", "residents", () -> {
                contentPanel.add(createResidentsPage(), "residents");
                showPage("residents");
            });
            addSidebarButton("📋", "Requests", "requests", () -> {
                contentPanel.add(createRequestsPage(), "requests");
                showPage("requests");
            });
            addSidebarButton("🔑", "Allocations", "allocations", () -> {
                contentPanel.add(createAllocationsPage(), "allocations");
                showPage("allocations");
            });

            addSidebarSection("Finance");
            addSidebarButton("💰", "Bills", "bills", () -> {
                contentPanel.add(createBillsPage(), "bills");
                showPage("bills");
            });
            addSidebarButton("💳", "Payments", "payments", () -> {
                contentPanel.add(createPaymentsPage(), "payments");
                showPage("payments");
            });

            addSidebarSection("System");
            addSidebarButton("👤", "Staff", "staff", () -> {
                contentPanel.add(createStaffPage(), "staff");
                showPage("staff");
            });
            addSidebarButton("📝", "Audit Logs", "audit", () -> {
                contentPanel.add(createAuditPage(), "audit");
                showPage("audit");
            });
            addSidebarButton("📦", "Load Demo Data", "demo", () -> {
                loadDemoData();
                contentPanel.add(createDashboardPage(), "dashboard");
                showPage("dashboard");
            });
        }

        // ============================================================
        // RECEPTIONIST PAGES
        // ============================================================
        private void buildReceptionistPages() {
            addSidebarSection("Overview");
            SidebarButton dashBtn = addSidebarButton("📊", "Dashboard", "dashboard", () -> {
                contentPanel.add(createDashboardPage(), "dashboard");
                showPage("dashboard");
            });
            contentPanel.add(createDashboardPage(), "dashboard");
            dashBtn.setSelected(true);

            addSidebarSection("Management");
            addSidebarButton("🏠", "Rooms & Beds", "rooms", () -> {
                contentPanel.add(createRoomsPage(), "rooms");
                showPage("rooms");
            });
            addSidebarButton("👥", "Residents", "residents", () -> {
                contentPanel.add(createResidentsPage(), "residents");
                showPage("residents");
            });
            addSidebarButton("📋", "Requests", "requests", () -> {
                contentPanel.add(createRequestsPage(), "requests");
                showPage("requests");
            });
            addSidebarButton("🔑", "Allocations", "allocations", () -> {
                contentPanel.add(createAllocationsPage(), "allocations");
                showPage("allocations");
            });

            addSidebarSection("Finance");
            addSidebarButton("💰", "Bills", "bills", () -> {
                contentPanel.add(createBillsPage(), "bills");
                showPage("bills");
            });
            addSidebarButton("💳", "Payments", "payments", () -> {
                contentPanel.add(createPaymentsPage(), "payments");
                showPage("payments");
            });
        }

        // ============================================================
        // RESIDENT PAGES
        // ============================================================
        private void buildResidentPages() {
            Resident resident = service.getResidentByUserId(currentUser.getId());

            addSidebarSection("My Account");
            SidebarButton profBtn = addSidebarButton("👤", "My Profile", "profile", () -> {
                contentPanel.add(createProfilePage(resident), "profile");
                showPage("profile");
            });
            contentPanel.add(createProfilePage(resident), "profile");
            profBtn.setSelected(true);

            addSidebarButton("📋", "My Requests", "myRequests", () -> {
                contentPanel.add(createMyRequestsPage(resident), "myRequests");
                showPage("myRequests");
            });
            addSidebarButton("🛏️", "My Allocation", "myAllocation", () -> {
                contentPanel.add(createMyAllocationPage(resident), "myAllocation");
                showPage("myAllocation");
            });

            addSidebarSection("Finance");
            addSidebarButton("💰", "My Bills", "myBills", () -> {
                contentPanel.add(createMyBillsPage(resident), "myBills");
                showPage("myBills");
            });
            addSidebarButton("💳", "My Payments", "myPayments", () -> {
                contentPanel.add(createMyPaymentsPage(resident), "myPayments");
                showPage("myPayments");
            });
        }

        private void showPage(String key) {
            sidebarButtons.forEach(b -> b.setSelected(false));
            contentLayout.show(contentPanel, key);
        }

        // ============================================================
        // DASHBOARD PAGE
        // ============================================================
        private JPanel createDashboardPage() {
            JPanel page = createPagePanel("Dashboard", "Overview of hostel operations");

            // Stats row
            JPanel statsRow = new JPanel(new GridLayout(1, 5, 16, 0));
            statsRow.setOpaque(false);
            statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            statsRow.add(new StatCard("Total Rooms", String.valueOf(database.rooms.size()),
                    "Registered rooms", Theme.ACCENT_BLUE));
            statsRow.add(new StatCard("Total Beds", String.valueOf(service.totalBeds()),
                    "All bed capacity", Theme.ACCENT_PURPLE));
            statsRow.add(new StatCard("Occupied", String.valueOf(service.occupiedBeds()),
                    "Currently allocated", Theme.ACCENT_ORANGE));
            statsRow.add(new StatCard("Available", String.valueOf(service.availableBeds()),
                    "Ready to allocate", Theme.SECONDARY));
            statsRow.add(new StatCard("Residents", String.valueOf(database.residents.size()),
                    "Registered residents", Theme.ACCENT_PINK));

            page.add(statsRow);
            page.add(Box.createVerticalStrut(24));

            // Second row stats
            JPanel statsRow2 = new JPanel(new GridLayout(1, 4, 16, 0));
            statsRow2.setOpaque(false);
            statsRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            statsRow2.setAlignmentX(Component.LEFT_ALIGNMENT);

            long pendingRequests = database.requests.values().stream()
                    .filter(r -> r.getStatus() == RequestStatus.PENDING).count();
            long unpaidBills = database.bills.values().stream()
                    .filter(b -> b.getStatus() != BillStatus.PAID).count();

            statsRow2.add(new StatCard("Pending Requests", String.valueOf(pendingRequests),
                    "Awaiting review", Theme.WARNING));
            statsRow2.add(new StatCard("Unpaid Bills", String.valueOf(unpaidBills),
                    "Outstanding bills", Theme.DANGER));
            statsRow2.add(new StatCard("Outstanding",
                    String.format("₹%.0f", service.totalOutstanding()),
                    "Total fees due", Theme.ACCENT_RED));
            statsRow2.add(new StatCard("Maintenance", String.valueOf(service.maintenanceBeds()),
                    "Beds in maintenance", Theme.TEXT_SECONDARY));

            page.add(statsRow2);
            page.add(Box.createVerticalStrut(24));

            // Recent allocations table
            JLabel recentLabel = new JLabel("Recent Allocations");
            recentLabel.setFont(Theme.FONT_SUBTITLE);
            recentLabel.setForeground(Theme.TEXT_PRIMARY);
            recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(recentLabel);
            page.add(Box.createVerticalStrut(12));

            String[] columns = {"ID", "Resident ID", "Location", "Date", "Status"};
            List<Allocation> recentAllocs = database.allocations.values().stream()
                    .sorted(Comparator.comparing(Allocation::getId).reversed())
                    .limit(10).collect(Collectors.toList());

            Object[][] data = new Object[recentAllocs.size()][5];
            for (int i = 0; i < recentAllocs.size(); i++) {
                Allocation a = recentAllocs.get(i);
                data[i] = new Object[]{a.getId(), a.getResidentId(),
                        service.getBedLocation(a.getBedId()),
                        a.getAllocationDate().toString(), a.getStatus().toString()};
            }

            JScrollPane table = createModernTable(columns, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // ROOMS PAGE
        // ============================================================
        private JPanel createRoomsPage() {
            JPanel page = createPagePanel("Rooms Management", "Add and manage hostel rooms");

            // Add Room Form
            RoundedPanel formCard = createFormCard("Add New Room");

            JPanel formGrid = new JPanel(new GridLayout(2, 2, 16, 12));
            formGrid.setOpaque(false);

            ModernTextField roomNumField = new ModernTextField("Room number (e.g., 101)");
            ModernTextField floorField = new ModernTextField("Floor number");
            ModernTextField typeField = new ModernTextField("Type (Single/Double/Triple)");
            ModernTextField feeField = new ModernTextField("Monthly fee");

            formGrid.add(wrapField("Room Number", roomNumField));
            formGrid.add(wrapField("Floor", floorField));
            formGrid.add(wrapField("Room Type", typeField));
            formGrid.add(wrapField("Monthly Fee (₹)", feeField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton addBtn = new ModernButton("Add Room", Theme.PRIMARY, Theme.TEXT_WHITE);
            addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            addBtn.addActionListener(e -> {
                try {
                    Room room = service.addRoom(roomNumField.getText(),
                            Integer.parseInt(floorField.getText()),
                            typeField.getText(),
                            Double.parseDouble(feeField.getText()));
                    service.audit(currentUser.getId(), "ADD_ROOM", "ROOM", room.getId(), "Room added");
                    FileDatabase.save(database);
                    showToast(this, "Room added successfully!", true);
                    contentPanel.add(createRoomsPage(), "rooms");
                    showPage("rooms");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });

            formCard.add(addBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Rooms Table
            JLabel tableTitle = new JLabel("All Rooms");
            tableTitle.setFont(Theme.FONT_SUBTITLE);
            tableTitle.setForeground(Theme.TEXT_PRIMARY);
            tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(tableTitle);
            page.add(Box.createVerticalStrut(12));

            String[] cols = {"ID", "Room No", "Floor", "Type", "Fee (₹)", "Status", "Beds"};
            List<Room> rooms = new ArrayList<>(database.rooms.values());
            Object[][] data = new Object[rooms.size()][7];
            for (int i = 0; i < rooms.size(); i++) {
                Room r = rooms.get(i);
                long bedCount = database.beds.values().stream()
                        .filter(b -> b.getRoomId() == r.getId()).count();
                data[i] = new Object[]{r.getId(), r.getRoomNumber(), r.getFloorNumber(),
                        r.getRoomType(), String.format("%.2f", r.getMonthlyFee()),
                        r.getStatus().toString(), bedCount};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // BEDS PAGE
        // ============================================================
        private JPanel createBedsPage() {
            JPanel page = createPagePanel("Beds Management", "Add beds and manage maintenance");

            // Add Bed Form
            RoundedPanel formCard = createFormCard("Add New Bed");

            JPanel formGrid = new JPanel(new GridLayout(1, 2, 16, 12));
            formGrid.setOpaque(false);

            // Room dropdown
            JComboBox<String> roomCombo = new JComboBox<>();
            roomCombo.setFont(Theme.FONT_INPUT);
            database.rooms.values().forEach(r ->
                    roomCombo.addItem(r.getId() + " - Room " + r.getRoomNumber()));

            ModernTextField bedNumField = new ModernTextField("Bed number (e.g., A, B, 1)");

            formGrid.add(wrapField("Select Room", roomCombo));
            formGrid.add(wrapField("Bed Number", bedNumField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton addBtn = new ModernButton("Add Bed", Theme.PRIMARY, Theme.TEXT_WHITE);
            addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            addBtn.addActionListener(e -> {
                try {
                    String selected = (String) roomCombo.getSelectedItem();
                    if (selected == null) throw new ValidationException("Select a room.");
                    long roomId = Long.parseLong(selected.split(" - ")[0].trim());
                    Bed bed = service.addBed(roomId, bedNumField.getText());
                    service.audit(currentUser.getId(), "ADD_BED", "BED", bed.getId(), "Bed added");
                    FileDatabase.save(database);
                    showToast(this, "Bed added successfully!", true);
                    contentPanel.add(createBedsPage(), "beds");
                    showPage("beds");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });

            formCard.add(addBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Beds Table
            JLabel tableTitle = new JLabel("All Beds");
            tableTitle.setFont(Theme.FONT_SUBTITLE);
            tableTitle.setForeground(Theme.TEXT_PRIMARY);
            tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(tableTitle);
            page.add(Box.createVerticalStrut(12));

            String[] cols = {"ID", "Room", "Bed No", "Status", "Occupied"};
            List<Bed> beds = new ArrayList<>(database.beds.values());
            Object[][] data = new Object[beds.size()][5];
            for (int i = 0; i < beds.size(); i++) {
                Bed b = beds.get(i);
                Room room = database.rooms.get(b.getRoomId());
                data[i] = new Object[]{b.getId(),
                        room != null ? room.getRoomNumber() : "?",
                        b.getBedNumber(), b.getStatus().toString(),
                        service.isBedOccupied(b.getId()) ? "Yes" : "No"};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // RESIDENTS PAGE
        // ============================================================
        private JPanel createResidentsPage() {
            JPanel page = createPagePanel("Residents", "View all registered residents");

            // Register new resident button
            ModernButton regBtn = new ModernButton("+ Register New Resident", Theme.PRIMARY, Theme.TEXT_WHITE);
            regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            regBtn.addActionListener(e -> showRegisterResidentDialog());
            page.add(regBtn);
            page.add(Box.createVerticalStrut(20));

            String[] cols = {"ID", "Name", "Email", "Phone", "College", "Emergency", "Registered"};
            List<Resident> residents = new ArrayList<>(database.residents.values());
            Object[][] data = new Object[residents.size()][7];
            for (int i = 0; i < residents.size(); i++) {
                Resident r = residents.get(i);
                data[i] = new Object[]{r.getId(), r.getFullName(), r.getEmail(),
                        r.getPhone(), r.getCollegeName(), r.getEmergencyContact(),
                        r.getRegistrationDate().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // REQUESTS PAGE
        // ============================================================
        private JPanel createRequestsPage() {
            JPanel page = createPagePanel("Room Requests", "Review and manage room allocation requests");

            String[] cols = {"ID", "Resident ID", "Preferred Type", "Date", "Status", "Remarks"};
            List<RoomRequest> requests = new ArrayList<>(database.requests.values());
            Object[][] data = new Object[requests.size()][6];
            for (int i = 0; i < requests.size(); i++) {
                RoomRequest r = requests.get(i);
                data[i] = new Object[]{r.getId(), r.getResidentId(), r.getPreferredRoomType(),
                        r.getRequestDate().toString(), r.getStatus().toString(),
                        r.getRemarks() != null ? r.getRemarks() : "-"};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            page.add(table);
            page.add(Box.createVerticalStrut(24));

            // Review form
            RoundedPanel formCard = createFormCard("Review Request");
            JPanel formGrid = new JPanel(new GridLayout(1, 3, 16, 12));
            formGrid.setOpaque(false);

            ModernTextField reqIdField = new ModernTextField("Request ID");
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"APPROVED", "REJECTED"});
            statusCombo.setFont(Theme.FONT_INPUT);
            ModernTextField remarksField = new ModernTextField("Remarks");

            formGrid.add(wrapField("Request ID", reqIdField));
            formGrid.add(wrapField("Decision", statusCombo));
            formGrid.add(wrapField("Remarks", remarksField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton reviewBtn = new ModernButton("Submit Review", Theme.PRIMARY, Theme.TEXT_WHITE);
            reviewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            reviewBtn.addActionListener(e -> {
                try {
                    long reqId = Long.parseLong(reqIdField.getText());
                    RequestStatus status = statusCombo.getSelectedItem().equals("APPROVED")
                            ? RequestStatus.APPROVED : RequestStatus.REJECTED;
                    service.reviewRequest(reqId, currentUser.getId(), status, remarksField.getText());
                    service.audit(currentUser.getId(), "REVIEW_REQUEST", "ROOM_REQUEST", reqId, "Reviewed");
                    FileDatabase.save(database);
                    showToast(this, "Request reviewed!", true);
                    contentPanel.add(createRequestsPage(), "requests");
                    showPage("requests");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });

            formCard.add(reviewBtn);
            page.add(formCard);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // ALLOCATIONS PAGE
        // ============================================================
        private JPanel createAllocationsPage() {
            JPanel page = createPagePanel("Allocations", "Allocate beds and manage check-outs");

            // Allocate Form
            RoundedPanel formCard = createFormCard("Allocate Bed to Resident");

            JPanel formGrid = new JPanel(new GridLayout(1, 2, 16, 12));
            formGrid.setOpaque(false);

            JComboBox<String> residentCombo = new JComboBox<>();
            residentCombo.setFont(Theme.FONT_INPUT);
            database.residents.values().forEach(r ->
                    residentCombo.addItem(r.getId() + " - " + r.getFullName()));

            JComboBox<String> bedCombo = new JComboBox<>();
            bedCombo.setFont(Theme.FONT_INPUT);
            service.getAvailableBeds().forEach(b ->
                    bedCombo.addItem(b.getId() + " - " + service.getBedLocation(b.getId())));

            formGrid.add(wrapField("Resident", residentCombo));
            formGrid.add(wrapField("Available Bed", bedCombo));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            btnRow.setOpaque(false);

            ModernButton allocBtn = new ModernButton("Allocate", Theme.SECONDARY, Theme.TEXT_WHITE);
            allocBtn.addActionListener(e -> {
                try {
                    String rSel = (String) residentCombo.getSelectedItem();
                    String bSel = (String) bedCombo.getSelectedItem();
                    if (rSel == null || bSel == null)
                        throw new ValidationException("Select resident and bed.");
                    long resId = Long.parseLong(rSel.split(" - ")[0].trim());
                    long bedId = Long.parseLong(bSel.split(" - ")[0].trim());
                    Allocation alloc = service.allocateBed(resId, bedId, currentUser.getId());
                    service.audit(currentUser.getId(), "ALLOCATE_BED", "ALLOCATION", alloc.getId(), "Allocated");
                    FileDatabase.save(database);
                    showToast(this, "Bed allocated successfully!", true);
                    contentPanel.add(createAllocationsPage(), "allocations");
                    showPage("allocations");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            btnRow.add(allocBtn);

            formCard.add(btnRow);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Allocations Table
            JLabel tableTitle = new JLabel("All Allocations");
            tableTitle.setFont(Theme.FONT_SUBTITLE);
            tableTitle.setForeground(Theme.TEXT_PRIMARY);
            tableTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(tableTitle);
            page.add(Box.createVerticalStrut(12));

            String[] cols = {"ID", "Resident", "Location", "Start Date", "End Date", "Status", "Action"};
            List<Allocation> allocs = new ArrayList<>(database.allocations.values());
            Object[][] data = new Object[allocs.size()][7];
            for (int i = 0; i < allocs.size(); i++) {
                Allocation a = allocs.get(i);
                Resident res = database.residents.get(a.getResidentId());
                data[i] = new Object[]{a.getId(),
                        res != null ? res.getFullName() : "ID:" + a.getResidentId(),
                        service.getBedLocation(a.getBedId()),
                        a.getAllocationDate().toString(),
                        a.getVacatingDate() != null ? a.getVacatingDate().toString() : "-",
                        a.getStatus().toString(),
                        a.getStatus() == AllocationStatus.ACTIVE ? "Check-out" : "-"};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            page.add(table);
            page.add(Box.createVerticalStrut(16));

            // Checkout form
            RoundedPanel coCard = createFormCard("Check-out Resident");
            JPanel coGrid = new JPanel(new GridLayout(1, 1, 16, 12));
            coGrid.setOpaque(false);

            ModernTextField allocIdField = new ModernTextField("Allocation ID");
            coGrid.add(wrapField("Allocation ID", allocIdField));
            coCard.add(coGrid);
            coCard.add(Box.createVerticalStrut(16));

            ModernButton coBtn = new ModernButton("Check-out", Theme.ACCENT_RED, Theme.TEXT_WHITE);
            coBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            coBtn.addActionListener(e -> {
                try {
                    long allocId = Long.parseLong(allocIdField.getText());
                    service.checkout(allocId);
                    service.audit(currentUser.getId(), "CHECKOUT", "ALLOCATION", allocId, "Checked out");
                    FileDatabase.save(database);
                    showToast(this, "Checked out successfully!", true);
                    contentPanel.add(createAllocationsPage(), "allocations");
                    showPage("allocations");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            coCard.add(coBtn);
            page.add(coCard);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // BILLS PAGE
        // ============================================================
        private JPanel createBillsPage() {
            JPanel page = createPagePanel("Billing", "Create and manage monthly bills");

            // Create Bill Form
            RoundedPanel formCard = createFormCard("Create Monthly Bill");

            JPanel formGrid = new JPanel(new GridLayout(1, 4, 16, 12));
            formGrid.setOpaque(false);

            JComboBox<String> resCombo = new JComboBox<>();
            resCombo.setFont(Theme.FONT_INPUT);
            database.residents.values().forEach(r ->
                    resCombo.addItem(r.getId() + " - " + r.getFullName()));

            ModernTextField yearField = new ModernTextField("Year (e.g., 2025)");
            ModernTextField monthField = new ModernTextField("Month (1-12)");
            ModernTextField amountField = new ModernTextField("Amount");

            formGrid.add(wrapField("Resident", resCombo));
            formGrid.add(wrapField("Year", yearField));
            formGrid.add(wrapField("Month", monthField));
            formGrid.add(wrapField("Amount (₹)", amountField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton createBtn = new ModernButton("Create Bill", Theme.PRIMARY, Theme.TEXT_WHITE);
            createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            createBtn.addActionListener(e -> {
                try {
                    String sel = (String) resCombo.getSelectedItem();
                    if (sel == null) throw new ValidationException("Select a resident.");
                    long resId = Long.parseLong(sel.split(" - ")[0].trim());
                    int year = Integer.parseInt(yearField.getText());
                    int month = Integer.parseInt(monthField.getText());
                    double amount = Double.parseDouble(amountField.getText());
                    Bill bill = service.createBill(resId, YearMonth.of(year, month), amount);
                    service.audit(currentUser.getId(), "CREATE_BILL", "BILL", bill.getId(), "Bill created");
                    FileDatabase.save(database);
                    showToast(this, "Bill created successfully!", true);
                    contentPanel.add(createBillsPage(), "bills");
                    showPage("bills");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            formCard.add(createBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Bills Table
            String[] cols = {"ID", "Resident", "Month", "Total (₹)", "Paid (₹)", "Outstanding (₹)", "Due Date", "Status"};
            List<Bill> bills = new ArrayList<>(database.bills.values());
            Object[][] data = new Object[bills.size()][8];
            for (int i = 0; i < bills.size(); i++) {
                Bill b = bills.get(i);
                Resident res = database.residents.get(b.getResidentId());
                data[i] = new Object[]{b.getId(),
                        res != null ? res.getFullName() : "ID:" + b.getResidentId(),
                        b.getBillingMonth().toString(),
                        String.format("%.2f", b.getTotalAmount()),
                        String.format("%.2f", service.getPaidAmount(b.getId())),
                        String.format("%.2f", service.getOutstandingAmount(b.getId())),
                        b.getDueDate().toString(), b.getStatus().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // PAYMENTS PAGE
        // ============================================================
        private JPanel createPaymentsPage() {
            JPanel page = createPagePanel("Payments", "Record and view payment transactions");

            // Record Payment Form
            RoundedPanel formCard = createFormCard("Record Payment");

            JPanel formGrid = new JPanel(new GridLayout(2, 2, 16, 12));
            formGrid.setOpaque(false);

            JComboBox<String> billCombo = new JComboBox<>();
            billCombo.setFont(Theme.FONT_INPUT);
            database.bills.values().stream()
                    .filter(b -> b.getStatus() != BillStatus.PAID)
                    .forEach(b -> billCombo.addItem(b.getId() + " - " + b.getBillingMonth()
                            + " (Due: ₹" + String.format("%.2f", service.getOutstandingAmount(b.getId())) + ")"));

            ModernTextField payAmountField = new ModernTextField("Payment amount");
            JComboBox<String> methodCombo = new JComboBox<>(new String[]{"CASH", "UPI", "CARD", "BANK_TRANSFER"});
            methodCombo.setFont(Theme.FONT_INPUT);
            ModernTextField refField = new ModernTextField("Transaction reference (optional)");

            formGrid.add(wrapField("Bill", billCombo));
            formGrid.add(wrapField("Amount (₹)", payAmountField));
            formGrid.add(wrapField("Payment Method", methodCombo));
            formGrid.add(wrapField("Reference", refField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton payBtn = new ModernButton("Record Payment", Theme.SECONDARY, Theme.TEXT_WHITE);
            payBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            payBtn.addActionListener(e -> {
                try {
                    String sel = (String) billCombo.getSelectedItem();
                    if (sel == null) throw new ValidationException("Select a bill.");
                    long billId = Long.parseLong(sel.split(" - ")[0].trim());
                    double amount = Double.parseDouble(payAmountField.getText());
                    PaymentMethod method = PaymentMethod.valueOf((String) methodCombo.getSelectedItem());
                    String ref = refField.getText().isBlank() ? null : refField.getText();
                    Payment payment = service.recordPayment(billId, amount, method, ref, currentUser.getId());
                    service.audit(currentUser.getId(), "RECORD_PAYMENT", "PAYMENT", payment.getId(), "Payment recorded");
                    FileDatabase.save(database);
                    showToast(this, "Payment recorded successfully!", true);
                    contentPanel.add(createPaymentsPage(), "payments");
                    showPage("payments");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            formCard.add(payBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Payments Table
            String[] cols = {"ID", "Bill ID", "Amount (₹)", "Date", "Method", "Reference", "Status"};
            List<Payment> payments = new ArrayList<>(database.payments.values());
            Object[][] data = new Object[payments.size()][7];
            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                data[i] = new Object[]{p.getId(), p.getBillId(),
                        String.format("%.2f", p.getAmount()),
                        p.getPaymentDate().toString(), p.getMethod().toString(),
                        p.getTransactionReference() != null ? p.getTransactionReference() : "-",
                        p.getStatus().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // STAFF PAGE
        // ============================================================
        private JPanel createStaffPage() {
            JPanel page = createPagePanel("Staff Management", "Create and manage staff accounts");

            RoundedPanel formCard = createFormCard("Create Staff Account");

            JPanel formGrid = new JPanel(new GridLayout(2, 2, 16, 12));
            formGrid.setOpaque(false);

            ModernTextField nameField = new ModernTextField("Full name");
            ModernTextField emailField = new ModernTextField("Email address");
            ModernTextField phoneField = new ModernTextField("Phone number");
            ModernPasswordField passField = new ModernPasswordField("Password");

            formGrid.add(wrapField("Full Name", nameField));
            formGrid.add(wrapField("Email", emailField));
            formGrid.add(wrapField("Phone", phoneField));
            formGrid.add(wrapField("Password", passField));

            formCard.add(formGrid);
            formCard.add(Box.createVerticalStrut(12));

            JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            roleRow.setOpaque(false);
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"ADMIN", "RECEPTIONIST"});
            roleCombo.setFont(Theme.FONT_INPUT);
            roleRow.add(wrapField("Role", roleCombo));
            formCard.add(roleRow);
            formCard.add(Box.createVerticalStrut(16));

            ModernButton createBtn = new ModernButton("Create Staff", Theme.PRIMARY, Theme.TEXT_WHITE);
            createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            createBtn.addActionListener(e -> {
                try {
                    Role role = Role.valueOf((String) roleCombo.getSelectedItem());
                    User user = service.createUser(nameField.getText(), emailField.getText(),
                            phoneField.getText(), new String(passField.getPassword()), role);
                    service.audit(currentUser.getId(), "CREATE_STAFF", "USER", user.getId(), "Staff created");
                    FileDatabase.save(database);
                    showToast(this, "Staff account created!", true);
                    contentPanel.add(createStaffPage(), "staff");
                    showPage("staff");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            formCard.add(createBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // Users Table
            String[] cols = {"ID", "Name", "Email", "Phone", "Role", "Status"};
            List<User> users = new ArrayList<>(database.users.values());
            Object[][] data = new Object[users.size()][6];
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                data[i] = new Object[]{u.getId(), u.getFullName(), u.getEmail(),
                        u.getPhone(), u.getRole().toString(), u.getStatus().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // AUDIT PAGE
        // ============================================================
        private JPanel createAuditPage() {
            JPanel page = createPagePanel("Audit Logs", "System activity logs");

            String[] cols = {"ID", "User ID", "Action", "Record Type", "Record ID", "Date", "Details"};
            List<AuditLog> logs = database.auditLogs.values().stream()
                    .sorted(Comparator.comparing(AuditLog::getId).reversed())
                    .collect(Collectors.toList());
            Object[][] data = new Object[logs.size()][7];
            for (int i = 0; i < logs.size(); i++) {
                AuditLog l = logs.get(i);
                data[i] = new Object[]{l.getId(), l.getUserId(), l.getAction(),
                        l.getRecordType(), l.getRecordId(), l.getDate().toString(), l.getDetails()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // RESIDENT PAGES
        // ============================================================
        private JPanel createProfilePage(Resident resident) {
            JPanel page = createPagePanel("My Profile", "Your personal information");

            if (resident == null) {
                page.add(new JLabel("Profile not found."));
                return wrapInScrollPane(page);
            }

            RoundedPanel card = new RoundedPanel(Theme.BORDER_RADIUS, Theme.BG_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(600, 400));

            // Avatar
            JLabel avatar = new JLabel("👤");
            avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
            avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(avatar);
            card.add(Box.createVerticalStrut(16));

            JLabel nameLabel = new JLabel(resident.getFullName());
            nameLabel.setFont(Theme.FONT_TITLE);
            nameLabel.setForeground(Theme.TEXT_PRIMARY);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(nameLabel);
            card.add(Box.createVerticalStrut(24));

            addProfileRow(card, "📧 Email", resident.getEmail());
            addProfileRow(card, "📱 Phone", resident.getPhone());
            addProfileRow(card, "🎓 College", resident.getCollegeName());
            addProfileRow(card, "🚨 Emergency", resident.getEmergencyContact());
            addProfileRow(card, "📅 Registered", resident.getRegistrationDate().toString());

            // Outstanding balance
            double outstanding = service.getResidentBills(resident.getId()).stream()
                    .mapToDouble(b -> service.getOutstandingAmount(b.getId())).sum();
            addProfileRow(card, "💰 Outstanding", String.format("₹%.2f", outstanding));

            page.add(card);

            return wrapInScrollPane(page);
        }

        private void addProfileRow(JPanel card, String label, String value) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.DIVIDER));

            JLabel lbl = new JLabel(label);
            lbl.setFont(Theme.FONT_BODY);
            lbl.setForeground(Theme.TEXT_SECONDARY);
            row.add(lbl, BorderLayout.WEST);

            JLabel val = new JLabel(value);
            val.setFont(Theme.FONT_BODY_BOLD);
            val.setForeground(Theme.TEXT_PRIMARY);
            row.add(val, BorderLayout.EAST);

            card.add(row);
            card.add(Box.createVerticalStrut(8));
        }

        private JPanel createMyRequestsPage(Resident resident) {
            JPanel page = createPagePanel("My Requests", "Submit and view room requests");

            if (resident == null) return wrapInScrollPane(page);

            // Submit Request Form
            RoundedPanel formCard = createFormCard("Submit New Room Request");
            ModernTextField typeField = new ModernTextField("Preferred room type (Single/Double/Triple)");
            typeField.setMaximumSize(new Dimension(400, 42));
            typeField.setAlignmentX(Component.LEFT_ALIGNMENT);
            formCard.add(wrapField("Preferred Room Type", typeField));
            formCard.add(Box.createVerticalStrut(16));

            ModernButton submitBtn = new ModernButton("Submit Request", Theme.PRIMARY, Theme.TEXT_WHITE);
            submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            submitBtn.addActionListener(e -> {
                try {
                    RoomRequest req = service.createRoomRequest(resident.getId(), typeField.getText());
                    service.audit(currentUser.getId(), "CREATE_REQUEST", "ROOM_REQUEST", req.getId(), "Submitted");
                    FileDatabase.save(database);
                    showToast(this, "Request submitted!", true);
                    contentPanel.add(createMyRequestsPage(resident), "myRequests");
                    showPage("myRequests");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            formCard.add(submitBtn);
            page.add(formCard);
            page.add(Box.createVerticalStrut(24));

            // My Requests Table
            String[] cols = {"ID", "Preferred Type", "Date", "Status", "Remarks"};
            List<RoomRequest> myReqs = database.requests.values().stream()
                    .filter(r -> r.getResidentId() == resident.getId())
                    .collect(Collectors.toList());
            Object[][] data = new Object[myReqs.size()][5];
            for (int i = 0; i < myReqs.size(); i++) {
                RoomRequest r = myReqs.get(i);
                data[i] = new Object[]{r.getId(), r.getPreferredRoomType(),
                        r.getRequestDate().toString(), r.getStatus().toString(),
                        r.getRemarks() != null ? r.getRemarks() : "-"};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            page.add(table);

            return wrapInScrollPane(page);
        }

        private JPanel createMyAllocationPage(Resident resident) {
            JPanel page = createPagePanel("My Allocation", "Current room and bed assignment");

            if (resident == null) return wrapInScrollPane(page);

            Allocation alloc = service.getActiveAllocation(resident.getId());

            RoundedPanel card = new RoundedPanel(Theme.BORDER_RADIUS, Theme.BG_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(500, 300));

            if (alloc == null) {
                JLabel noAlloc = new JLabel("🛏️ No active allocation");
                noAlloc.setFont(Theme.FONT_SUBTITLE);
                noAlloc.setForeground(Theme.TEXT_SECONDARY);
                noAlloc.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(noAlloc);

                JLabel hint = new JLabel("Submit a room request to get allocated.");
                hint.setFont(Theme.FONT_BODY);
                hint.setForeground(Theme.TEXT_LIGHT);
                hint.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(Box.createVerticalStrut(8));
                card.add(hint);
            } else {
                JLabel icon = new JLabel("🛏️");
                icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                icon.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(icon);
                card.add(Box.createVerticalStrut(16));

                addProfileRow(card, "📍 Location", service.getBedLocation(alloc.getBedId()));
                addProfileRow(card, "📅 Allocated On", alloc.getAllocationDate().toString());
                addProfileRow(card, "✅ Status", alloc.getStatus().toString());
            }

            page.add(card);
            return wrapInScrollPane(page);
        }

        private JPanel createMyBillsPage(Resident resident) {
            JPanel page = createPagePanel("My Bills", "View your monthly bills");

            if (resident == null) return wrapInScrollPane(page);

            // Outstanding balance card
            double total = service.getResidentBills(resident.getId()).stream()
                    .mapToDouble(b -> service.getOutstandingAmount(b.getId())).sum();

            JPanel balanceRow = new JPanel(new GridLayout(1, 2, 16, 0));
            balanceRow.setOpaque(false);
            balanceRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
            balanceRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            balanceRow.add(new StatCard("Outstanding Balance", String.format("₹%.2f", total),
                    "Total amount due", Theme.ACCENT_RED));
            double totalPaid = service.getResidentBills(resident.getId()).stream()
                    .mapToDouble(b -> service.getPaidAmount(b.getId())).sum();
            balanceRow.add(new StatCard("Total Paid", String.format("₹%.2f", totalPaid),
                    "All time payments", Theme.SECONDARY));
            page.add(balanceRow);
            page.add(Box.createVerticalStrut(24));

            String[] cols = {"ID", "Month", "Total (₹)", "Paid (₹)", "Outstanding (₹)", "Due Date", "Status"};
            List<Bill> bills = service.getResidentBills(resident.getId());
            Object[][] data = new Object[bills.size()][7];
            for (int i = 0; i < bills.size(); i++) {
                Bill b = bills.get(i);
                data[i] = new Object[]{b.getId(), b.getBillingMonth().toString(),
                        String.format("%.2f", b.getTotalAmount()),
                        String.format("%.2f", service.getPaidAmount(b.getId())),
                        String.format("%.2f", service.getOutstandingAmount(b.getId())),
                        b.getDueDate().toString(), b.getStatus().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        private JPanel createMyPaymentsPage(Resident resident) {
            JPanel page = createPagePanel("My Payments", "View your payment history");

            if (resident == null) return wrapInScrollPane(page);

            String[] cols = {"ID", "Bill ID", "Amount (₹)", "Date", "Method", "Reference", "Status"};
            List<Payment> payments = service.getResidentPayments(resident.getId());
            Object[][] data = new Object[payments.size()][7];
            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                data[i] = new Object[]{p.getId(), p.getBillId(),
                        String.format("%.2f", p.getAmount()),
                        p.getPaymentDate().toString(), p.getMethod().toString(),
                        p.getTransactionReference() != null ? p.getTransactionReference() : "-",
                        p.getStatus().toString()};
            }

            JScrollPane table = createModernTable(cols, data);
            table.setAlignmentX(Component.LEFT_ALIGNMENT);
            table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
            page.add(table);

            return wrapInScrollPane(page);
        }

        // ============================================================
        // REGISTER RESIDENT DIALOG
        // ============================================================
        private void showRegisterResidentDialog() {
            JDialog dialog = new JDialog(this, "Register New Resident", true);
            dialog.setSize(480, 520);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
            panel.setBackground(Theme.BG_CARD);

            JLabel title = new JLabel("Register New Resident");
            title.setFont(Theme.FONT_SUBTITLE);
            title.setForeground(Theme.TEXT_PRIMARY);
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(title);
            panel.add(Box.createVerticalStrut(20));

            ModernTextField nameField = addFormField(panel, "Full Name", "Enter full name");
            ModernTextField emailField = addFormField(panel, "Email", "Enter email");
            ModernTextField phoneField = addFormField(panel, "Phone", "Enter phone");
            ModernTextField collegeField = addFormField(panel, "College", "Enter college name");
            ModernTextField emergencyField = addFormField(panel, "Emergency Contact", "Emergency number");

            JLabel passLabel = createFieldLabel("Password");
            panel.add(passLabel);
            panel.add(Box.createVerticalStrut(4));
            ModernPasswordField passField = new ModernPasswordField("Password (min 4 chars)");
            passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            passField.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(passField);
            panel.add(Box.createVerticalStrut(20));

            ModernButton regBtn = new ModernButton("Register", Theme.SECONDARY, Theme.TEXT_WHITE);
            regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            regBtn.addActionListener(e -> {
                try {
                    service.registerResident(nameField.getText(), emailField.getText(),
                            phoneField.getText(), collegeField.getText(), emergencyField.getText(),
                            new String(passField.getPassword()));
                    FileDatabase.save(database);
                    showToast(this, "Resident registered successfully!", true);
                    dialog.dispose();
                    contentPanel.add(createResidentsPage(), "residents");
                    showPage("residents");
                } catch (Exception ex) {
                    showToast(this, ex.getMessage(), false);
                }
            });
            panel.add(regBtn);

            dialog.add(panel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }

        // ============================================================
        // DEMO DATA
        // ============================================================
        private void loadDemoData() {
            try {
                if (database.rooms.isEmpty()) {
                    Room r1 = service.addRoom("101", 1, "Single", 5000);
                    Room r2 = service.addRoom("102", 1, "Double", 4000);
                    Room r3 = service.addRoom("201", 2, "Triple", 3000);
                    Room r4 = service.addRoom("202", 2, "Single", 5500);
                    Room r5 = service.addRoom("301", 3, "Double", 4500);

                    service.addBed(r1.getId(), "A");
                    service.addBed(r2.getId(), "A");
                    service.addBed(r2.getId(), "B");
                    service.addBed(r3.getId(), "A");
                    service.addBed(r3.getId(), "B");
                    service.addBed(r3.getId(), "C");
                    service.addBed(r4.getId(), "A");
                    service.addBed(r5.getId(), "A");
                    service.addBed(r5.getId(), "B");
                }

                if (database.residents.size() <= 0) {
                    service.registerResident("Rahul Kumar", "rahul@test.com", "9876543210",
                            "IIT Delhi", "9876543211", "pass123");
                    service.registerResident("Priya Sharma", "priya@test.com", "9876543212",
                            "NIT Trichy", "9876543213", "pass123");
                    service.registerResident("Arjun Patel", "arjun@test.com", "9876543214",
                            "BITS Pilani", "9876543215", "pass123");
                }

                service.audit(currentUser.getId(), "LOAD_DEMO", "SYSTEM", 0, "Demo data loaded");
                FileDatabase.save(database);
                showToast(this, "Demo data loaded successfully!", true);
            } catch (Exception e) {
                showToast(this, "Some demo data already exists: " + e.getMessage(), false);
            }
        }

        // ============================================================
        // HELPER METHODS
        // ============================================================
        private JPanel createPagePanel(String title, String subtitle) {
            JPanel page = new JPanel();
            page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
            page.setBackground(Theme.BG_MAIN);
            page.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(Theme.FONT_TITLE);
            titleLabel.setForeground(Theme.TEXT_PRIMARY);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(titleLabel);

            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(Theme.FONT_BODY);
            subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(subtitleLabel);
            page.add(Box.createVerticalStrut(24));

            return page;
        }

        private RoundedPanel createFormCard(String title) {
            RoundedPanel card = new RoundedPanel(Theme.BORDER_RADIUS, Theme.BG_CARD, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

            JLabel cardTitle = new JLabel(title);
            cardTitle.setFont(Theme.FONT_HEADING);
            cardTitle.setForeground(Theme.TEXT_PRIMARY);
            cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(cardTitle);
            card.add(Box.createVerticalStrut(16));

            return card;
        }

        private JPanel wrapField(String label, JComponent field) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);

            JLabel lbl = new JLabel(label);
            lbl.setFont(Theme.FONT_SMALL_BOLD);
            lbl.setForeground(Theme.TEXT_SECONDARY);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapper.add(lbl);
            wrapper.add(Box.createVerticalStrut(4));

            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (field instanceof JTextField) {
                field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            }
            wrapper.add(field);

            return wrapper;
        }

        private ModernTextField addFormField(JPanel card, String label, String placeholder) {
            JLabel lbl = createFieldLabel(label);
            card.add(lbl);
            card.add(Box.createVerticalStrut(4));

            ModernTextField field = new ModernTextField(placeholder);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(field);
            card.add(Box.createVerticalStrut(12));

            return field;
        }

        private JLabel createFieldLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(Theme.FONT_SMALL_BOLD);
            label.setForeground(Theme.TEXT_SECONDARY);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            return label;
        }

        private JPanel wrapInScrollPane(JPanel content) {
            JScrollPane scroll = new JScrollPane(content);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.getViewport().setBackground(Theme.BG_MAIN);

            // Modern scrollbar
            scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override protected void configureScrollBarColors() {
                    thumbColor = new Color(200, 200, 200);
                    trackColor = Theme.BG_MAIN;
                }
                @Override protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }
                @Override protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }
                private JButton createZeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(0, 0));
                    return button;
                }
            });

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.add(scroll, BorderLayout.CENTER);
            return wrapper;
        }
    }

    // Helper for login screen
    private static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_SMALL_BOLD);
        label.setForeground(Theme.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // ============================================================
    // MAIN METHOD
    // ============================================================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Anti-aliased text globally
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(MainApp::new);
    }
}
