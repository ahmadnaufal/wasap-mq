# WASAP-MQ

Aplikasi chat yang kami buat terdiri dari dua buah program utama, yaitu Server side dan Client side.

## A. PENJELASAN ARSITEKTUR
### 1. Server side
Aplikasi bagian Server menyimpan implementasi RPC yang akan dipanggil dari Client untuk prosedur yang menggunakan akses Database, seperti register, login, membuat grup baru, menambahkan teman, mengundang teman ke grup, dan meninggalkan grup. Database yang digunakan diimplementasikan dalam bentuk file SQLite, dengan nama `wasap_mq.db`.

Untuk implementasi RPC, kami mendefinisikan queue baru pada server RabbitMQ dengan nama "rpc_queue". Queue ini akan digunakan untuk menyimpan setiap pesan yang berisi request dari client yang akan menggunakan RPC. Setiap pemanggilan RPC akan mengembalikan respon terkait yang berisi status pemanggilan RPC (sukses, gagal, atau lainnya) dan value lain yang dibutuhkan.

### 2. Client side
Pada sisi Client, ada dua sisi. Pertama, queue dengan nama `rpc_queue` yang berguna untuk melakukan pemanggilan (request) pada RPC Server, dan interaksi antar client yang menggunakan dua buah exchange, yaitu `notification` dan `message`.

## B. PETUNJUK INSTALASI/BUILD
### 1. Client
Client diimplementasikan dalam bahasa JAVA. Untuk melakukan compile client, gunakan IntelliJ IDEA untuk membuka file project .iml. Kemudian, klik tombol Build pada Menu Build -> Rebuild Project untuk melakukan build Project. Setelah itu, jalankan dengan menggunakan menu Run -> Run...

Untuk menjalankan JAR client yang telah disediakan, masuk ke dalam path `DVa_Client\out\artifacts\DVa_Client_jar` kemudian ketikkan perintah pada Terminal `java -jar DVa_Client.jar`.

### 2. Server
Server diimplementasikan dalam bahasa Python. Sehingga, untuk menjalankan Server, hanya tinggal mengeksekusi script Python melalui Python interpreter di command line dengan perintah `python Main.py` pada folder Server.

## C. MELAKUKAN TES
Untuk melakukan pengujian. Ikuti langkah sebagai berikut:

1. Jalankan Server Program terlebih dahulu pada Terminal
2. Jalankan 2 Client Program
3. Pada salah satu client yang dijalankan, coba registrasi dengan memasukkan username `test` dan password `test` pada field yang tersedia, dan klik tombol **Register**. Login dengan username dan password yang sama, dan klik tombol **Login**
4. Pada client yang lain, registrasi dengan memasukkan username `test2` dan password `test2` pada field yang tersedia, dan klik tombol **Register**. Login dengan username dan password yang sama.
5. Setelah keduanya masuk ke Lobby, alihkan ke program dengan username `test`, masuk ke tab Friends, kemudian ketik username `test2` pada field yang tersedia di atas tombol **Add Friend**. Klik tombol tersebut untuk menambahkan `test2` menjadi teman. Daftar teman yang ada di sebelah kiri jendela program akan bertambah. Begitu juga pada jendela program dengan username `test2`.
6. Untuk memulai chat, pilih salah satu daftar teman `test` atau `test2`. Kemudian klik tombol **Chat** untuk membuka jendela chat.
7. Ketik chat pada salah satu jendela, kemudian klik **Enter** untuk mengirim chat ke teman chat. Buka jendela chat teman yang lain, maka chat yang telah dikirim akan ditampilkan.
8. Tutup kedua jendela chat, kemudian buka tab **Group**. Coba buat grup pada salah satu jendela Lobby dengan mengetikkan nama grup pada field yang tersedia, dan klik tombol **Create Group**. Daftar grup yang ada di sebelah kiri jendela program akan bertambah.
9. Buka jendela group chat dengan memilih nama grup pada sisi kiri jendela, dan klik tombol **Chat**
10. Setelah masuk ke jendela Group Chat, secara otomatis username pembuat group akan tersedia di sisi pojok kanan atas. Untuk menambahkan member grup, pilih daftar teman pada dropdown, kemudian klik tombol **Add Member**.
11. Mulai chat dengan mengetikkan pesan pada field yang tersedia dan mengklik **Enter**.

