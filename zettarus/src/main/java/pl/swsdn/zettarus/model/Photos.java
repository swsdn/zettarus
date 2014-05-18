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

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;

@Repository
public class Photos {

	private final File rootFolder;
	private ImmutableSortedSet<PhotoEntry> files;
	private long populationMark;

	public class PhotoEntry implements Comparable<PhotoEntry> {
		public final String path;
		public final DateTime modificationTime;

		PhotoEntry(Path path) {
			this.path = getFileRelativePath(path);
			this.modificationTime = new DateTime(path.toFile().lastModified());
		}

		private String getFileRelativePath(Path file) {
			URI relative = rootFolder.toURI().relativize(file.toUri());
			return relative.getPath();
		}

		@Override
		public int compareTo(PhotoEntry o) {
			// reversed
			return o.modificationTime.compareTo(modificationTime);
		}
	}

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
		final Builder<PhotoEntry> filesBuilder = ImmutableSortedSet.<PhotoEntry> naturalOrder();
		SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				filesBuilder.add(new PhotoEntry(file));
				return super.visitFile(file, attrs);
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
		return System.currentTimeMillis() - populationMark > 60000;
	}

	private void isFolderValid(File rootFolder) {
		isTrue(rootFolder.exists() && rootFolder.isDirectory(),
				String.format("Path (%s) must be directory", rootFolder));
	}

	public File getPath() {
		return rootFolder;
	}

	public List<PhotoEntry> getPhotos() {
		populateCache();
		return files.asList();
	}

	public int getCount() {
		return files.size();
	}

}
