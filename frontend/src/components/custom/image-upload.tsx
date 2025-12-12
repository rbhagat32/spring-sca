import { XIcon } from "lucide-react";
import { useState } from "react";

import { Button } from "@/components/ui/button";
import { Dropzone, DropzoneEmptyState } from "@/components/ui/dropzone";
import {
  ImageCrop,
  ImageCropApply,
  ImageCropContent,
  ImageCropReset,
} from "@/components/ui/image-crop";
import { cn } from "@/utils/cn";

interface ImageUploadProps {
  onFileChange?: (file: File | null) => void;
}

export function ImageUpload({ onFileChange }: ImageUploadProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [croppedImage, setCroppedImage] = useState<string | null>(null);
  const [isCropping, setIsCropping] = useState(false);

  const handleDrop = (files: File[]) => {
    const file = files[0];
    if (file) {
      setSelectedFile(file);
      setCroppedImage(null);
      setIsCropping(true);
    }
  };

  const handleReset = () => {
    setSelectedFile(null);
    setCroppedImage(null);
    setIsCropping(false);
    onFileChange?.(null);
  };

  const handleCrop = async (dataUrl: string) => {
    setCroppedImage(dataUrl);
    setIsCropping(false);

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

  // file cropped successfully -> render preview
  if (croppedImage && !isCropping) {
    return (
      <div
        className={cn(
          "bg-muted/30 flex flex-col items-center justify-center gap-3 rounded-lg px-8 py-6 text-center ring ring-zinc-700"
        )}
      >
        <img
          src={croppedImage}
          alt="Cropped"
          className="size-20 rounded-full object-cover shadow-sm"
        />

        <Button onClick={handleReset} variant="outline" size="sm" type="button" className="mt-2">
          <span className="mb-0.5 block">Remove</span>
          <XIcon className="size-4" />
        </Button>
      </div>
    );
  }

  // else -> render cropping screen
  return (
    <>
      {/* blurred background */}
      <div className="fixed inset-0 z-40 bg-transparent backdrop-blur-md" />

      {/* crop window */}
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div className="relative flex w-full max-w-lg flex-col items-center justify-center rounded-xl border-2 bg-zinc-900 p-4 shadow-lg">
          <ImageCrop
            aspect={1}
            circularCrop
            file={selectedFile}
            maxImageSize={10 * 1024 * 1024}
            onCrop={handleCrop}
          >
            <ImageCropContent className="max-h-[70vh]" />

            <div className="mt-4 flex items-center justify-end gap-2">
              <ImageCropApply />
              <ImageCropReset />
              <Button onClick={handleReset} size="icon" type="button" variant="ghost">
                <XIcon className="size-4 text-red-500" />
              </Button>
            </div>
          </ImageCrop>
        </div>
      </div>
    </>
  );
}
