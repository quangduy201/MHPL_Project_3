# PROJECT 3: Quản lý thành viên

## 1. Chức năng 1: Quản lý thành viên
### Vy - Dũng - Duy
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 1.1 Hiển thị danh sách
#### 1.2 Thêm mới thông tin (thủ công / excel)
#### 1.3 Sửa/ Cập nhật thông tin
#### 1.4 Xóa thành viên - Xóa theo điều kiện
#### 1.5 Vào khu vực học tập
#### 1.6 Mượn, trả thiết bị
#### 1.7 Cảnh báo vi phạm

## 2. Chức năng 2: Quản lý thiết bị
### Hưng - An - Giàu
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 2.1 Hiển thị danh sách
#### 2.2 Thêm mới thông tin thiết bị (thủ công / excel)
#### 2.3 Sửa/ Cập nhật thông tin thiết bị
#### 2.4 Xóa thiết bị - Xóa theo điều kiện

## 3. Chức năng 3: Xử lý vi phạm
### Hưng - An - Giàu
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 3.1 Hiển thị danh sách
#### 3.2 Thêm mới thông tin xử lý vi phạm
#### 3.3 Sửa/ Cập nhật thông tin xử lý vi phạm
#### 3.4 Xóa xử lý vi phạm - Xóa theo điều kiện

## 4. Chức năng 4: Thống kê
### Hưng - An - Giàu
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 4.1 Số lượng thành viên vào khu học tập theo: thời gian, khoa, ngành.
#### 4.2 Thiết bị đã được mượn theo thời gian, tên thiết bị
#### 4.3 Thiết bị đang được mượn theo thời gian, tên thiết bị.
#### 4.4 Xử lý vi phạm theo:
* Đã được xử lý (nếu có bồi thường tiền thì tính tổng tiền bồi thường)
* Đang xử lý

## 5. Chức năng 5: Đăng kí thành viên
### Hiếu - Hoa - Hoàng
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 5.1 Đăng kí tài khoản thành viên
#### 5.2 Đăng nhập thành viên
#### 5.3 Quên mật khẩu
#### 5.4 Xem hồ sơ thành viên:
* Đổi mật khẩu
* Xem trạng thái vi phạm
* Xem đặt chỗ thiết bị
* Xem thiết bị đang mượn

## 6. Chức năng 6: Đặt chỗ mượn thiết bị phía thành viên
### Vy - Dũng - Duy
* GUI, DAL, BLL
  * `Sequence Diagram`
  * `Chụp màn hình các file code và GUI`

#### 6.1 Tìm kiếm thiết bị theo tên
#### 6.2 Chọn đặt chỗ mượn thiết bị
* Thiết bị được đặt chỗ mượn nếu sau thời gian đặt chỗ 1 giờ thì tự hủy đặt chỗ
* Thiết bị đang được đặt chỗ thì không được cho mượn hoặc cho mượn (trong ngày đặt chỗ). Thiết bị đó vẫn được đặt chỗ mượn cho ngày hôm khác.

> [!IMPORTANT]
> * **Mỗi khi bắt đầu làm, mở terminal lên sử dụng lệnh ```git pull origin main``` để tránh việc merge bị conflict :(.**
> * **Cập nhật Database**
> * **Project 3 chạy SDK 18 hoặc lớn hơn**
> * **DEADLINE 15/05 --- Lớp bờ du <3.**
