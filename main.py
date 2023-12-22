from fastapi import FastAPI, HTTPException, File, UploadFile
from fastapi.responses import JSONResponse
from google.cloud import storage
from google.oauth2 import service_account
import os

# FastAPI
app = FastAPI()

# Google Cloud Storage
service_account_path = 'basuwara-backup-154ff9b5c33a.json'
storage_client = storage.Client.from_service_account_json(service_account_path)

@app.post("/upload-image/")
async def upload_image(file: UploadFile = File(...)):
    try:
        # Google Cloud Storage
        bucket_name = 'basuwara-bucket-backup'
        bucket = storage_client.get_bucket(bucket_name)
        blob = bucket.blob(file.filename)

        # Save file to Google Cloud Storage
        blob.upload_from_string(
            file.file.read(),
            content_type=file.content_type
        )

        return JSONResponse(status_code=200, content={"message": "Image uploaded successfully", "file_name": file.filename})
    except Exception as e:
        return JSONResponse(status_code=500, content={"message": str(e)})

@app.get("/get-image/{image_name}")
async def get_image(image_name: str):
    try:
        # Google Cloud Storage
        bucket_name = 'basuwara-bucket-backup'
        bucket = storage_client.get_bucket(bucket_name)
        blob = bucket.blob(image_name)

        # Read file from Google Cloud Storage
        content = blob.download_as_text()

        return JSONResponse(status_code=200, content={"message": "Image fetched successfully", "content": content})
    except Exception as e:
        return JSONResponse(status_code=500, content={"message": str(e)})

@app.get("/get-all-images/")
async def get_all_images():
    try:
        # Google Cloud Storage
        bucket_name = 'basuwara-bucket-backup'
        blobs = storage_client.list_blobs(bucket_name)

        images = []
        for blob in blobs:
            images.append(blob.name)

        return JSONResponse(status_code=200, content={"message": "Images fetched successfully", "images": images})
    except Exception as e:
        return JSONResponse(status_code=500, content={"message": str(e)})

@app.delete("/delete-image/{image_name}")
async def delete_image(image_name: str):
    try:
        # Google Cloud Storage
        bucket_name = 'basuwara-bucket-backup'
        bucket = storage_client.get_bucket(bucket_name)
        blob = bucket.blob(image_name)

        # Delete file from Google Cloud Storage
        blob.delete()

        return JSONResponse(status_code=200, content={"message": "Image deleted successfully", "file_name": image_name})
    except Exception as e:
        return JSONResponse(status_code=500, content={"message": str(e)})