package pers.wuchao.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 *
 */
public final class Files {
	/**
	 * 
	 */
	private Files() {
		// TODO Auto-generated constructor stub
	}

	static public byte[] read(InputStream in) throws IOException {
		byte[] temp = new byte[4096];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BufferedInputStream bin = new BufferedInputStream(in);
		for (;;) {
			int len = bin.read(temp);
			if (len > -1)
				buffer.write(temp, 0, len);
			else
				break;
		}
		return buffer.toByteArray();
	}

	static public String[] scanPackage(String packageName) throws Exception {
		packageName = packageName.replaceAll("\\.", "/");
		URL url = Thread.currentThread().getContextClassLoader().getResource(packageName);
		url = new URL(URLDecoder.decode(url.toString(), "UTF-8"));
		FileSystemProvider provider = null;
		if (url.getProtocol().equals("jar")) {
			provider = getZipFSProvider();
			if (provider != null) {
				try (FileSystem fs = provider.newFileSystem(
						Paths.get(url.getPath().replaceFirst("file:/", "").replaceFirst("!.*", "")), new HashMap<>())) {
					return walkFileTree(fs.getPath(packageName), null).toArray(new String[0]);
				} catch (Exception e) {
					throw e;
				}
			}
		} else if (url.getProtocol().equals("file")) {
			int end = url.getPath().lastIndexOf(packageName);
			String basePath = url.getPath().substring(1, end);
			return walkFileTree(Paths.get(url.getPath().replaceFirst("/", "")), Paths.get(basePath))
					.toArray(new String[0]);
		}
		return null;
	}

	private static List<String> walkFileTree(Path path, Path basePath) throws IOException {
		final List<String> result = new ArrayList<>();
		java.nio.file.Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			private String packageName = Objects.isNull(basePath) ? "" : basePath.toString();

			@Override
			public FileVisitResult visitFile(Path arg0, BasicFileAttributes arg1) throws IOException {
				if (arg0.toString().endsWith(".class")) {
					result.add(arg0.toString().replace(packageName, "").substring(1).replace("\\", "/")
							.replace(".class", "").replace("/", "."));
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path arg0, BasicFileAttributes arg1) throws IOException {
				return FileVisitResult.CONTINUE;
			}

		});
		return result;
	}

	static public FileSystemProvider getZipFSProvider() {
		for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
			if ("jar".equals(provider.getScheme()))
				return provider;
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		for (String c : scanPackage("pers"))
			System.out.println(c);

	}

}