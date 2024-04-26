package com.example.project_3.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Resource {
    public static URL getJarURL() {
        return Resource.class.getProtectionDomain().getCodeSource().getLocation();
    }

    public static Path getPathOutsideJAR() {
        try {
            URL url = getJarURL();
            String[] urlParts = url.toString().split(":");
            String fullPath = urlParts[urlParts.length - 1];
            int indexOfBootInf = fullPath.indexOf("!BOOT-INF");
            String jarPath = fullPath.substring(0, indexOfBootInf != -1 ? indexOfBootInf : fullPath.length() - 1);
            return new File(jarPath).getParentFile().toPath();
        } catch (Exception e) {
            Log.error(Resource.class, "Error while getting the path outside the JAR: " + e);
        }
        return null;
    }

    public static boolean exists(String relativePath, boolean insideJAR) {
        URL url = null;
        if (insideJAR)
            url = Resource.class.getResource("/" + relativePath);
        else {
            try {
                Path path = getPathOutsideJAR();
                if (path != null) {
                    path = path.resolve("resources" + relativePath);
                    url = path.toUri().toURL();
                }
            } catch (MalformedURLException e) {
                return false;
            }
        }
        return url != null;
    }

    public static URL getURLFromRoot(String relativePath, boolean insideJAR) {
        if (insideJAR)
            return Resource.class.getResource("/" + relativePath);
        try {
            Path path = getPathOutsideJAR();
            if (path != null) {
                path = path.resolve(relativePath);
                return path.toUri().toURL();
            }
            Log.error(Resource.class, "Can't get the path outside the JAR: " + relativePath);
        } catch (MalformedURLException e) {
            Log.error(Resource.class, "Error while getting the URL from the root: " + e);
        }
        return null;
    }

    public static InputStream getInputStreamFromRoot(String relativePath, boolean insideJAR) {
        if (insideJAR)
            return Resource.class.getResourceAsStream("/" + relativePath);
        try {
            Path path = getPathOutsideJAR();
            if (path != null) {
                path = path.resolve(relativePath);
                return Files.newInputStream(path);
            }
            Log.error(Resource.class, "Can't get the path outside the JAR: " + relativePath);
        } catch (IOException e) {
            Log.error(Resource.class, "Error while getting the InputStream from the root: " + e);
        }
        return null;
    }

    public static Path getPathFromRoot(String relativePath, boolean insideJAR) {
        try {
            URL url = getURLFromRoot(relativePath, insideJAR);
            if (url != null)
                return Paths.get(url.toURI());
            Log.error(Resource.class, "Can't get the URL from the root: " + relativePath);
        } catch (URISyntaxException e) {
            Log.error(Resource.class, "Error while getting the path from the root: " + e);
        }
        return null;
    }

    public static URL getURLFromResource(String relativePath, boolean insideJAR) {
        if (insideJAR)
            return getURLFromRoot(relativePath, true);
        return getURLFromRoot("resources" + File.separator + relativePath, false);
    }

    public static InputStream getInputStreamFromResource(String relativePath, boolean insideJAR) {
        if (insideJAR)
            return getInputStreamFromRoot(relativePath, true);
        return getInputStreamFromRoot("resources" + File.separator + relativePath, false);
    }

    public static Path getPathFromResource(String relativePath, boolean insideJAR) {
        if (insideJAR)
            return getPathFromRoot(relativePath, true);
        return getPathFromRoot("resources" + File.separator + relativePath, false);
    }

    public static String getResourcePath(String relativePath, boolean insideJAR) {
        Path path = getPathFromResource(relativePath, insideJAR);
        if (path != null)
            return path.toString();
        Log.error(Resource.class, "Can't get the path from resource: " + relativePath);
        return null;
    }

    public static String getResourcePath(boolean insideJAR) {
        return getResourcePath("", insideJAR);
    }

    public static Properties loadProperties(String relativePath, boolean insideJAR) {
        try (InputStream inputStream = getInputStreamFromResource(relativePath, insideJAR)) {
            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);
                return properties;
            }
            Log.error(Resource.class, "Can't get the input stream from resource: " + relativePath);
        } catch (IOException e) {
            Log.error(Resource.class, "Error while loading the properties file: " + e);
        }
        return null;
    }

    public static Properties loadProperties(String relativePath) {
        return loadProperties(relativePath, true);
    }

    public static void copyResource(String sourceRelativePath, String destinationRelativePath, boolean overwrite) {
        Path destinationPath = getPathFromResource(destinationRelativePath, false);
        if (destinationPath == null) {
            Log.error(Resource.class, "Destination path is not specified: " + destinationRelativePath);
            return;
        }
        if (Files.notExists(destinationPath) || overwrite) {
            try (InputStream inputStream = getInputStreamFromResource(sourceRelativePath, true)) {
                if (inputStream != null) {
                    setReadOnly(destinationPath, false);
                    Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    setReadOnly(destinationPath, true);
                } else
                    Log.error(Resource.class, "Resource not found: " + sourceRelativePath);
            } catch (IOException e) {
                Log.error(Resource.class, "Error while copying the resource from '" + sourceRelativePath + "' to '" + destinationRelativePath + "': " + e);
            }
        }
    }

    public static void copyResource(String sourceRelativePath, String destinationRelativePath) {
        copyResource(sourceRelativePath, destinationRelativePath, false);
    }

    public static boolean isReadOnly(Path filePath) {
        return Files.isRegularFile(filePath) && !Files.isWritable(filePath);
    }

    public static void setReadOnly(Path filePath, boolean makeReadOnly) {
        if (isReadOnly(filePath) == makeReadOnly)
            return;
        boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
        try {
            if (isPosix) {
                Set<PosixFilePermission> permissions = new HashSet<>();
                permissions.add(PosixFilePermission.OWNER_READ);

                if (makeReadOnly) {
                    Files.setPosixFilePermissions(filePath, permissions);
                } else {
                    permissions.add(PosixFilePermission.OWNER_WRITE);
                    Files.setPosixFilePermissions(filePath, permissions);
                }
            } else {
                DosFileAttributeView dosView = Files.getFileAttributeView(filePath, DosFileAttributeView.class);
                if (dosView != null) {
                    dosView.setReadOnly(makeReadOnly);
                } else {
                    Log.error(Resource.class, "DOS/Windows file attribute view not supported for: " + filePath);
                }
            }
        } catch (IOException e) {
            Log.error(Resource.class, "Failed to set file as read-only: " + filePath + " - " + e.getMessage());
        }
    }
}
