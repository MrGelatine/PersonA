import time

import numpy as np
from flask import Flask
from flask_restful import reqparse, Resource, Api
import os
import torch
import io, base64
from PIL import Image
import torchvision
from torchvision import transforms

def run_alignment(image_path):
  import dlib
  from pixel2style2pixel.scripts.align_all_parallel import align_face
  predictor = dlib.shape_predictor(r'D:\GitHub\PersonA\flaskProject\shape_predictor_68_face_landmarks.dat')
  aligned_image = align_face(filepath=image_path, predictor=predictor)
  print("Aligned image has shape: {}".format(aligned_image.size))
  return aligned_image

def load_latent_borders(border_path):
    res = {}
    with open(border_path, 'r') as f:
        for line in f.readlines():
            desir = line.split(':')
            vector_name = desir[0]
            borders = desir[1][1:-3].split(';')
            borders[0] = float(borders[0])
            borders[1] = float(borders[1])
            res[vector_name] = borders
    return res
app = Flask(__name__)
api = Api(app)

parser = reqparse.RequestParser()
parser.add_argument('faceBase64')
transform = transforms.Compose([
            transforms.Resize((256, 256)),
            transforms.ToTensor(),
            transforms.Normalize([0.5, 0.5, 0.5], [0.5, 0.5, 0.5])])
model = torch.jit.load("face_encoder.pt")

age_latent_vector = np.random.rand(18,512)
blond_hair_latent_vector = np.random.rand(18,512)
with open("ageLatentVector.npy",'wb') as f:
    np.save(f, age_latent_vector)
with open("blondHairVector.npy",'wb') as f:
    np.save(f, blond_hair_latent_vector)


def load_latent_vectors(path):
    latent_vectors = {}
    for dirpath, dirnames, filenames in os.walk(path):
        for file in filenames:
            vector_name = file[:-17]
            vector = np.load(os.path.join(dirpath, file))
            latent_vectors[vector_name] = vector
    return latent_vectors

prev = None
class Extractor(Resource):
    def __init__(self):
        self.latent_vectors = load_latent_vectors(r'D:\Downloads\CelebA\latent_vectors')

    def latent_vector_coefficient(self, latent_vector, embending):
        latent_vector_fl = latent_vector.flatten()
        embending_fl = embending.flatten()
        l1 = np.dot(embending_fl, latent_vector_fl)
        l2 = np.dot(latent_vector_fl, latent_vector_fl)
        return l1 / l2
    def post(self):
        args = parser.parse_args()
        faceBase64 = args["faceBase64"]
        img = Image.open(io.BytesIO(base64.decodebytes(bytes(faceBase64, "utf-8"))))
        img.save('my-image.jpg')
        print(np.array(img))
        tensor = transform(img)
        with torch.no_grad():
            output = model(tensor.unsqueeze(0)).squeeze().detach().numpy()
        print(f"Min value:{np.min(output)} Max value: {np.max(output)}")
        latent_output = {}
        vector_borders = load_latent_borders(r'D:\GitHub\PersonA\flaskProject\latent_vectors_borders.txt')
        for latent_vector_name, latent_vector in self.latent_vectors.items():
            vector_coefficient = self.latent_vector_coefficient(latent_vector, output)
            current_borders = vector_borders[latent_vector_name]
            vector_coefficient = -vector_coefficient / current_borders[0] if vector_coefficient < 0 else vector_coefficient / current_borders[1]
            latent_output[latent_vector_name] = vector_coefficient
        real_output = {}
        real_output["face"] = latent_output

        return real_output


api.add_resource(Extractor, '/faceInfo/')

if __name__ == '__main__':
    os.chdir('./pixel2style2pixel')
    app.run()

