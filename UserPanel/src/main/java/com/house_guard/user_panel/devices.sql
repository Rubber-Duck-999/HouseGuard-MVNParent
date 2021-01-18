create table devices(
    device_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, 
    name VARCHAR(100), 
    mac VARCHAR(20), 
    status VARCHAR(10)
);
