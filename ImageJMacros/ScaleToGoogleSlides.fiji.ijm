function getFormat(filename){
	lower = toLowerCase(filename);
	if (endsWith(lower, '.jpg')){
		return 'JPEG';	
	} if (endsWith(lower, '.tif')){
		return 'TIFF';	
	} if (endsWith(lower, '.png')){
		return 'PNG';	
	} if (endsWith(lower, '.gif')){
		return 'GIF';	
	}
	return 'JPG';
}

dir1 = getDirectory("Choose_Source Directory ");
dirName = File.getName(dir1);
dir2 = replace(dir1, dirName, dirName + "_scaled");
if(!File.exists(dir2)){
	File.makeDirectory(dir2); 
}
megapixels = 20 * 1024 * 1024;
list = getFileList(dir1);
setBatchMode(true);

for (i=0; i<list.length; i++) {
	if(endsWith(list[i], 'jpg')){
		open(dir1+list[i]);
		width = getWidth;
	  	height = getHeight;
		aspect = width / height;
		new_width = sqrt(megapixels * aspect);
		new_height = sqrt(megapixels / aspect);
		run("Scale...", "x=- y=- width=" + new_width +" height=" + new_height + " interpolation=Bicubic average create");
		saveAs(getFormat(list[i]), dir2+list[i]);
		showProgress(i+1, list.length);
		close();
	}
}

