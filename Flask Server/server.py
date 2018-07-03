import requests
import base64
import json
import sys
from flask import Flask, render_template, request, jsonify, request, send_from_directory
from flask_restful import Resource, Api, reqparse
import os
import random
import string
import numpy as np
import skimage.io as io
import numpy as np
import pytesseract
import cv2


app = Flask(__name__)
app.config['SECRET_KEY'] = 'Arjuna'
api = Api(app)

UPLOAD_FOLDER = os.path.basename('Uploads')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


######################### APP ROUTES ##############################

@app.route('/')
def index():
	return "Vyakhya - 1.0 (Manas Bhardwaj and CDAC))"


# # GET
# @app.route('/api/info_back_to_android')
# def get_predicted_text(self):
# 	return "Got ur image successfully on the server"

def convertImage(imgData1, upload_loc):
	with open( upload_loc, "wb") as output:
		output.write(base64.b64decode(imgData1))

# POST
@app.route('/uploadform', methods=['POST'])
def form():
    return render_template('form.html')

class ImageUpload(Resource):
    def post(self):
        data = request.get_json()
        # random_string = data["latitude"] + "_" + data["longitude"]


        random_string = "Manas"

        upload_loc = os.path.join(UPLOAD_FOLDER, random_string + ".jpg")

        imgData = data['image']
        convertImage(imgData, upload_loc)
        sz = len(imgData) / (1024 * 1024)

        decoded_image = cv2.imread(upload_loc)

        # print(type(np.frombuffer(decoded_image, dtype=np.uint8)))
        # # decoded_image = io.imread(np.frombuffer(decoded_image, dtype=np.uint8))
        # decoded_image = np.frombuffer(decoded_image, dtype=np.uint8)

        extracted_data = pytesseract.image_to_string(decoded_image, lang='hin+eng')
        # fh.write(image_data)
        # fh.close()
        # data = {'Image Data' : 'Image received of Size ' + str(sz) + ' MB'}

        data = {"Image Data": extracted_data}

        data = jsonify(data)

        return data



api.add_resource(ImageUpload, '/uploadimage')




if __name__ == "__main__":
    app.run(host='0.0.0.0', port = 5000)
