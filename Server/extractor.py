from argparse import Namespace
import time
import sys
import pprint
import numpy as np
from PIL import Image
import torch
import torchvision.transforms as transforms

from pixel2style2pixel.datasets import augmentations
from pixel2style2pixel.utils.common import tensor2im, log_input_image
from pixel2style2pixel.models.psp import pSp

def run_on_batch(inputs, net, latent_mask=None):
    if latent_mask is None:
        result_batch = net.encoder(inputs.to("cpu").float())
        print(result_batch.shape)
    else:
        result_batch = []
        for image_idx, input_image in enumerate(inputs):
            # get latent vector to inject into our input image
            vec_to_inject = np.random.randn(1, 512).astype('float32')
            _, latent_to_inject = net(torch.from_numpy(vec_to_inject).to("cuda"),
                                      input_code=True,
                                      return_latents=True)
            # get output image with injected style vector
            res = net(input_image.unsqueeze(0).to("cuda").float(),
                      latent_mask=latent_mask,
                      inject_latent=latent_to_inject)
            result_batch.append(res)
        result_batch = torch.cat(result_batch, dim=0)
    return result_batch

EXPERIMENT_ARGS = {
        "model_path": '/content/drive/MyDrive/Model/psp_ffhq_encode.pt',
        "image_path": "notebooks/images/input_img.jpg",
        "transform": transforms.Compose([
            transforms.Resize((256, 256)),
            transforms.ToTensor(),
            transforms.Normalize([0.5, 0.5, 0.5], [0.5, 0.5, 0.5])])
    }

def run_extractor():
    model_path = EXPERIMENT_ARGS['model_path']
    ckpt = torch.load(model_path, map_location='cpu')
    opts = ckpt['opts']

    # update the training options
    opts['checkpoint_path'] = model_path
    #opts['device'] = 'cpu'
    if 'learn_in_w' not in opts:
        opts['learn_in_w'] = False
    if 'output_size' not in opts:
        opts['output_size'] = 1024

    opts = Namespace(**opts)
    net = pSp(opts)
    net.eval()
    print('Model successfully loaded!')

    image_path = EXPERIMENT_ARGS["image_path"]
    original_image = Image.open(image_path)
    img_transforms = EXPERIMENT_ARGS['transform']
    transformed_image = img_transforms(original_image)

    with torch.no_grad():
        print(net.encoder(transformed_image.unsqueeze(0)).shape)