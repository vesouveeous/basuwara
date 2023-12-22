# FastAPI Google Cloud Storage API

## Clone This Repository

Install Dependencies
Gunakan salah satu dari dua perintah di bawah ini untuk menginstal dependensi:

## Menggunakan pip
```sh
pip install fastapi
pip install uvicorn
pip install --upgrade google-cloud-storage 
```
### if install error use this
```sh
python -m pip install fastapi
python -m pip install uvicorn
python -m pip install --upgrade google-cloud-storage 
```
### using this to run API
```sh
uvicorn main:app --reload
```
## Endpoint API

### POST
Endpoint: /upload-image/<br />
Method: POST<br />**
Deskripsi: Mengunggah gambar ke Google Cloud Storage.<br />

### GET
Endpoint: /get-image/{image_name}<br />
Method: GET<br />
Deskripsi: Mengambil konten gambar dari Google Cloud Storage.<br />

### GETALL
Endpoint: /get-all-images/
Method: GET
Deskripsi: Mengambil daftar semua gambar yang tersimpan di Google Cloud Storage.

### DELETE
Endpoint: /delete-image/{image_name}<br />
Method: DELETE<br />
Deskripsi: Menghapus gambar dari Google Cloud Storage.<br />
