from argparse import Namespace
import time
import sys
import pprint
import numpy as np
from PIL import Image
import torch
import torchvision.transforms as transforms
from torch.utils.mobile_optimizer import optimize_for_mobile

from datasets import augmentations
from utils.common import tensor2im, log_input_image
from models.psp import pSp

def run_on_batch(inputs, net, latent_mask=None):
    if latent_mask is None:
        result_batch = net(inputs.to("cuda").float(), randomize_noise=False)
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

if __name__ == '__main__':
    sys.path.append(".")
    sys.path.append("..")
    experiment_type = 'ffhq_encode'

    MODEL_PATHS = {
        "ffhq_encode": {"id": "1bMTNWkh5LArlaWSc_wa8VKyq2V42T2z0", "name": "psp_ffhq_encode.pt"},
        "ffhq_frontalize": {"id": "1_S4THAzXb-97DbpXmanjHtXRyKxqjARv", "name": "psp_ffhq_frontalization.pt"},
        "celebs_sketch_to_face": {"id": "1lB7wk7MwtdxL-LL4Z_T76DuCfk00aSXA", "name": "psp_celebs_sketch_to_face.pt"},
        "celebs_seg_to_face": {"id": "1VpEKc6E6yG3xhYuZ0cq8D2_1CbT0Dstz", "name": "psp_celebs_seg_to_face.pt"},
        "celebs_super_resolution": {"id": "1ZpmSXBpJ9pFEov6-jjQstAlfYbkebECu", "name": "psp_celebs_super_resolution.pt"},
        "toonify": {"id": "1YKoiVuFaqdvzDP5CZaqa3k5phL-VDmyz", "name": "psp_ffhq_toonify.pt"}
    }

    EXPERIMENT_DATA_ARGS = {
        "ffhq_encode": {
            "model_path": '/content/drive/MyDrive/Model/psp_ffhq_encode.pt',
            "image_path": "notebooks/images/input_img.jpg",
            "transform": transforms.Compose([
                transforms.Resize((256, 256)),
                transforms.ToTensor(),
                transforms.Normalize([0.5, 0.5, 0.5], [0.5, 0.5, 0.5])])
        }
    }

    EXPERIMENT_ARGS = EXPERIMENT_DATA_ARGS[experiment_type]

    model_path = EXPERIMENT_ARGS['model_path']
    ckpt = torch.load(model_path, map_location='cpu')

    opts = ckpt['opts']

    # update the training options
    opts['checkpoint_path'] = model_path
    if 'learn_in_w' not in opts:
        opts['learn_in_w'] = False
    if 'output_size' not in opts:
        opts['output_size'] = 1024

    opts = Namespace(**opts)
    net = pSp(opts)
    net.eval()
    net.cuda()
    print('Model successfully loaded!')

    image_path = EXPERIMENT_DATA_ARGS[experiment_type]["image_path"]
    original_image = Image.open(image_path)
    if opts.label_nc == 0:
        original_image = original_image.convert("RGB")
    else:
        original_image = original_image.convert("L")

    original_image.resize((256, 256))

    img_transforms = EXPERIMENT_ARGS['transform']
    transformed_image = img_transforms(input_image)

    latent_mask = None

    with torch.no_grad():
        result_image = net(inputs.to("cuda").float(), randomize_noise=False)

    input_vis_image = log_input_image(transformed_image, opts)
    output_image = tensor2im(result_image)
    res = np.concatenate([np.array(input_vis_image.resize((256, 256))),
                              np.array(output_image.resize((256, 256)))], axis=1)
    res_image = Image.fromarray(res)