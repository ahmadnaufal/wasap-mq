Aplikasi chat yang kami buat terdiri dari dua buah program utama, yaitu Server side dan Client side.

A. PENJELASAN ARSITEKTUR
1. Server side
Aplikasi bagian Server menyimpan implementasi RPC yang akan dipanggil dari Client untuk prosedur yang menggunakan akses Database, seperti register, login, membuat grup baru, menambahkan teman, mengundang teman ke grup, dan meninggalkan grup. Database yang digunakan diimplementasikan dalam bentuk file SQLite, dengan nama "wasap_mq.db".
Untuk implementasi RPC, kami mendefinisikan queue baru pada server RabbitMQ dengan nama "rpc_queue". Queue ini akan digunakan untuk menyimpan setiap pesan yang berisi request dari client yang akan menggunakan RPC. Setiap pemanggilan RPC akan mengembalikan respon terkait yang berisi status pemanggilan RPC (sukses, gagal, atau lainnya) dan value lain yang dibutuhkan.

2. Client side
Pada sisi Client, 

B. PETUNJUK INSTALASI/BUILD
1. Client
   Client diimplementasikan dalam bahasa JAVA. Untuk melakukan compile client, gunakan IntelliJ IDEA untuk membuka file project .iml. Kemudian, klik tombol Build pada Menu Build -> Rebuild Project untuk melakukan build Project. Setelah itu, jalankan dengan menggunakan menu Run -> Run...
2. Server
   Server diimplementasikan dalam bahasa Python. Sehingga, untuk menjalankan Server.
