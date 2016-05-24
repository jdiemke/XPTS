package org.polygonize.ats.core.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageFactory {

    private static ImageFactory instance_;

    public enum ImageType {

        SOURCE_OPERATOR, SOURCE_OPERATOR_SELECTED, SOURCE_OPERATOR_ACTIVE, SOURCE_OPERATOR_SELECTED_AND_ACTIVE,

        SINK_OPERATOR, SINK_OPERATOR_SELECTED, SINK_OPERATOR_ACTIVE, SINK_OPERATOR_SELECTED_AND_ACTIVE,

        SINK_TEXTURE, SINK_TEXTURE_SELECTED,

        LOAD_OPERATOR, LOAD_OPERATOR_SELECTED,

        PROCESS_OPERATOR, PROCESS_OPERATOR_SELECTED, PROCESS_OPERATOR_ACTIVE, PROCESS_OPERATOR_SELECTED_AND_ACTIVE,

        PIXEL_FONT, PIXEL_FONT_SHADOW
    };

    private HashMap<ImageType, BufferedImage> enumToImageIconMapping_;

    public static ImageFactory getInstance() {
        if (instance_ == null) {
            instance_ = new ImageFactory();
        }
        return instance_;
    }

    private ImageFactory() {

        enumToImageIconMapping_ = new HashMap<ImageType, BufferedImage>();

        try {
            // enumToImageIconMapping_.put(ImageType.SOURCE_OPERATOR,
            // ImageIO.read(new File("resources/source.png")));
            // URL url = this.getClass().getResource
            enumToImageIconMapping_.put(ImageType.SOURCE_OPERATOR,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/source.png")));
            enumToImageIconMapping_.put(ImageType.SOURCE_OPERATOR_SELECTED,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sourceSelected.png")));
            enumToImageIconMapping_.put(ImageType.SOURCE_OPERATOR_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sourceActive.png")));
            enumToImageIconMapping_.put(ImageType.SOURCE_OPERATOR_SELECTED_AND_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sourceSelectedActive.png")));

            enumToImageIconMapping_.put(ImageType.SINK_OPERATOR,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sink2.png")));
            enumToImageIconMapping_.put(ImageType.SINK_OPERATOR_SELECTED,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sinkSelected2.png")));
            enumToImageIconMapping_.put(ImageType.SINK_OPERATOR_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sinkActive.png")));
            enumToImageIconMapping_.put(ImageType.SINK_OPERATOR_SELECTED_AND_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sinkSelectedActive.png")));

            enumToImageIconMapping_.put(ImageType.SINK_TEXTURE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sinkTexture.png")));
            enumToImageIconMapping_.put(ImageType.SINK_TEXTURE_SELECTED,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sinkTextureSelected.png")));

            enumToImageIconMapping_.put(ImageType.LOAD_OPERATOR,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/source2.png")));
            enumToImageIconMapping_.put(ImageType.LOAD_OPERATOR_SELECTED,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/sourceSelected2.png")));

            enumToImageIconMapping_.put(ImageType.PROCESS_OPERATOR,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/process.png")));
            enumToImageIconMapping_.put(ImageType.PROCESS_OPERATOR_SELECTED,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/processSelected.png")));
            enumToImageIconMapping_.put(ImageType.PROCESS_OPERATOR_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/processActive.png")));
            enumToImageIconMapping_.put(ImageType.PROCESS_OPERATOR_SELECTED_AND_ACTIVE,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/operators/processSelectedActive.png")));

            enumToImageIconMapping_.put(ImageType.PIXEL_FONT,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/font/pixelfont.png")));
            enumToImageIconMapping_.put(ImageType.PIXEL_FONT_SHADOW,
                    ImageIO.read(this.getClass().getResourceAsStream("/images/font/pixelfontshadow.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(ImageType icon) {
        return enumToImageIconMapping_.get(icon);
    }

}
