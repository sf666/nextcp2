import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';

@Component({
    selector: 'image-cropper',
    templateUrl: './image-cropper.component.html',
    styleUrls: ['./image-cropper.component.scss']
})
export class ImageCropperComponent implements OnInit {

    @ViewChild("image", { static: false })
    public imageElement: ElementRef;

    @Input("src")
    public imageSource: string;

    public croppedImageData: string;

    public constructor() {
    }

    public ngAfterViewInit() {

        // the desired aspect ratio of our output image (width / height)
        const outputImageAspectRatio = 1;

        // this image will hold our source image data
        const inputImage = new Image();
        inputImage.crossOrigin = "anonymous";

        // we want to wait for our image to load
        inputImage.onload = () => {
            // let's store the width and height of our image
            const inputWidth = inputImage.naturalWidth;
            const inputHeight = inputImage.naturalHeight;

            // get the aspect ratio of the input image
            const inputImageAspectRatio = inputWidth / inputHeight;

            // if it's bigger than our target aspect ratio
            let outputWidth = inputWidth;
            let outputHeight = inputHeight;
            if (inputImageAspectRatio > outputImageAspectRatio) {
                outputWidth = inputHeight * outputImageAspectRatio;
            } else if (inputImageAspectRatio < outputImageAspectRatio) {
                outputHeight = inputWidth / outputImageAspectRatio;
            }

            // create a canvas that will present the output image
            const outputImage = document.createElement('canvas');

            // set it to the same size as the image
            outputImage.width = outputWidth;
            outputImage.height = outputHeight;

            // draw our image at position 0, 0 on the canvas
            const ctx = outputImage.getContext('2d');
            ctx.drawImage(inputImage, 0, 0);

            this.croppedImageData = outputImage.toDataURL();
            // show both the image and the canvas
            //            document.body.appendChild(inputImage);
            //            document.body.appendChild(outputImage);
        };

        // start loading our image
        inputImage.src = this.imageSource;

        /*
        this.cropper = new Cropper(this.imageElement.nativeElement, {
            zoomable: false,
            scalable: false,
            autoCrop: false,
            checkCrossOrigin: true,
            aspectRatio: 1,
            crop: (event) => {
                console.log("cropping image ... ");
                const canvas = this.cropper.getCroppedCanvas();
                this.imageSource = canvas.toDataURL("image/png");
                console.log(event.detail.width);
                console.log(event.detail.height);
            }
        });
        */
    }

    public ngOnInit() { }
}
