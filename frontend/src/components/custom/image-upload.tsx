import { useState } from "react";
import { Button } from "@/components/ui/button";
import { XIcon } from "lucide-react";
import { Dropzone, DropzoneEmptyState } from "@/components/ui/dropzone";
import {
  ImageCrop,
  ImageCropApply,
  ImageCropContent,
  ImageCropReset,
} from "@/components/ui/image-crop";

interface ImageUploadProps {
  onFileChange?: (file: File | null) => void;
}

export function ImageUpload({ onFileChange }: ImageUploadProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [croppedImage, setCroppedImage] = useState<string | null>(null);

  const handleDrop = (files: File[]) => {
    const file = files[0];
    if (file) {
      setSelectedFile(file);
      setCroppedImage(null);
    }
  };

  const handleReset = () => {
    setSelectedFile(null);
    setCroppedImage(null);
    onFileChange?.(null);
  };

  const handleCrop = async (dataUrl: string) => {
    setCroppedImage(dataUrl);

    const res = await fetch(dataUrl);
    const blob = await res.blob();
    const croppedFile = new File([blob], selectedFile?.name || "cropped.png", {
      type: "image/png",
    });

    onFileChange?.(croppedFile);
  };

  // no file selected -> dropzone
  if (!selectedFile) {
    return (
      <Dropzone
        accept={{ "image/*": [] }}
        onDrop={handleDrop}
        onError={console.error}
        className="w-full"
      >
        <DropzoneEmptyState />
      </Dropzone>
    );
  }

  // file cropped -> render preview
  if (croppedImage) {
    return (
      <div className="flex items-center gap-3">
        <img src={croppedImage} alt="Cropped" className="size-10 rounded-full object-cover" />
        <Button onClick={handleReset} size="icon" type="button" variant="ghost">
          <XIcon className="size-4" />
        </Button>
      </div>
    );
  }

  // else -> render cropping screen
  return (
    <div className="space-y-4">
      <ImageCrop
        aspect={1}
        circularCrop
        file={selectedFile}
        maxImageSize={10 * 1024 * 1024}
        onCrop={handleCrop}
        className="fixed top-0 left-0"
      >
        <ImageCropContent className="max-w-md" />
        <div className="flex items-center gap-2">
          <ImageCropApply />
          <ImageCropReset />
          <Button onClick={handleReset} size="icon" type="button" variant="ghost">
            <XIcon className="size-4 text-red-500" />
          </Button>
        </div>
      </ImageCrop>
    </div>
  );
}
