package pl.swsdn.zettarus.model;

import static org.springframework.util.Assert.isTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@Component
public class RootFolder {

	File rootFolder;
	ImmutableList<String> files;
	ImmutableList<String> directories;

	@Autowired
	public RootFolder(@Value("#{'${rootFolder.path}'}") final File rootFolder) throws IOException {
		isFolderValid(rootFolder);
		this.rootFolder = rootFolder;
		//populateCache();
	}

	private void populateCache() {
		final Builder<String> filesBuilder = ImmutableList.<String> builder();
		final Builder<String> directoriesBuilder = ImmutableList.<String> builder();

		final String rootFolderPathName = rootFolder.getAbsolutePath();
		SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				if (!dir.toFile().equals(rootFolder)) {
					directoriesBuilder.add(dir.toString().replace(rootFolderPathName, ""));
					return FileVisitResult.SKIP_SUBTREE;
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				filesBuilder.add(file.toUri().toString());
				return super.visitFile(file, attrs);
			}
		};
		try {
			Files.walkFileTree(Paths.get(rootFolder.toURI()), visitor);
			this.directories = directoriesBuilder.build();
			this.files = filesBuilder.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void isFolderValid(File rootFolder) {
		isTrue(rootFolder.exists() && rootFolder.isDirectory(),
				String.format("Path (%s) must be directory", rootFolder));
	}

	public File getPath() {
		return rootFolder;
	}

	public List<String> getFiles() {
		//populateCache();
		return files;
	}

	public List<String> getDirectories() {
		//populateCache();
		return directories;
	}

}
