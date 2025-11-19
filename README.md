----------------------------------TABLE----------------------------------------------------
CREATE TABLE User(
	username VARCHAR(20) PRIMARY KEY,
    password VARCHAR(20),
    role VARCHAR(10)
);

CREATE TABLE SanPham(
	ma_san_pham VARCHAR(20) PRIMARY KEY,
	ten_san_pham VARCHAR(100),
    username VARCHAR(20),
    image VARCHAR(100),
    so_luong INT,
    gia INT,
    FOREIGN KEY (username) REFERENCES User(username)
);

CREATE TABLE HoaDon(
	ma_hoa_don VARCHAR(20),
    username VARCHAR(20),
    ma_san_pham VARCHAR(20),
    so_luong INT,
    PRIMARY KEY (ma_hoa_don, username),
    FOREIGN KEY (username) REFERENCES User(username),
    FOREIGN KEY (ma_san_pham) REFERENCES SanPham(ma_san_pham) ON DELETE CASCADE
);

CREATE TABLE GioHang(
    username VARCHAR(20),
    ma_san_pham VARCHAR(20),
    so_luong INT,
    PRIMARY KEY (ma_san_pham, username),
    FOREIGN KEY (username) REFERENCES User(username),
    FOREIGN KEY (ma_san_pham) REFERENCES SanPham(ma_san_pham) ON DELETE CASCADE
);


----------------------------------INSERT DATA-----------------------------------
insert into sanpham
values ('1', 'Bim Bim Khoai Tây', 'seller1', 'com/example/demo/images/snack.jpg', 6, 100000),
	('2', 'Siêu Cà Rốt', 'seller1', 'com/example/demo/images/carrot.jpg', 5, 5000),
    ('3', 'Màu Sáp', 'seller3', 'com/example/demo/images/sap_mau.jpg', 5, 5000),
    ('4', 'Bút chì', 'seller2', 'com/example/demo/images/pencil.jpg', 5, 10000),
    ('5', 'Bàn phím PC', 'seller3', 'com/example/demo/images/keyboard.jpg', 6, 50000),
    ('6', 'Bút chì màu', 'seller1', 'com/example/demo/images/colour_pencil.jpg', 10, 5000),
    ('7', 'Sổ Sketch', 'seller2', 'com/example/demo/images/sketch_book.jpg', 3, 100000);
insert into user 
values ('cust', '123', 'customer'),
       ('seller1', '123', 'seller'),
       ('seller2', '123', 'seller'),
       ('seller3', '123', 'seller');
