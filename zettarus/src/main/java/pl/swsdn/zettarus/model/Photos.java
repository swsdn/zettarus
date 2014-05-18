package pl.swsdn.zettarus.model;

import static org.springframework.util.Assert.isTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@Repository
public class Photos {

	File rootFolder;
	ImmutableList<String> files;
	private long populationMark;

	@Autowired
	public Photos(@Value("#{'${rootFolder.path}'}") final File rootFolder) throws IOException {
		isFolderValid(rootFolder);
		this.rootFolder = rootFolder;
		populateCache();
	}

	private void populateCache() {
		if (!isPopulateRequired()) {
			return;
		}
		final Builder<String> filesBuilder = ImmutableList.<String> builder();
		SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				filesBuilder.add(getFileRelativePath(file));
				return super.visitFile(file, attrs);
			}

			private String getFileRelativePath(Path file) {
				URI relative = rootFolder.toURI().relativize(file.toUri());
				return relative.getPath();
			}
		};
		try {
			Files.walkFileTree(Paths.get(rootFolder.toURI()), visitor);
			this.files = filesBuilder.build();
			populationMark = System.currentTimeMillis();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isPopulateRequired() {
		return System.currentTimeMillis() - populationMark > 10000;
	}

	private void isFolderValid(File rootFolder) {
		isTrue(rootFolder.exists() && rootFolder.isDirectory(),
				String.format("Path (%s) must be directory", rootFolder));
	}

	public File getPath() {
		return rootFolder;
	}

	public List<String> getPhotos() {
		populateCache();
		return files;
	}

}
