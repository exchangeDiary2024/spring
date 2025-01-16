export default class ImageUtil {
    static preLoadImage(images) {
        images.forEach((image) => {
            const img = new Image();
            img.src = image;
        });
    }
}
