# Nft-generation-minting-backend
This is a Spring Boot application, provides REST API calls for the generation of NFTs and for minting them. There is no persistence of the data, files are written in filesystem directories to store and use them 'on the fly'.

The generation is made calling a python script, so to make the app work, a python environment needs to be set up.
These are the required libraries:

ctypes.wintypes

image_slicer

random

PIL

datetime

