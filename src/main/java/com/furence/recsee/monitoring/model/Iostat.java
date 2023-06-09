package com.furence.recsee.monitoring.model;

import java.util.ArrayList;

import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.Shell;
import org.hyperic.sigar.cmd.SigarCommandBase;
import org.hyperic.sigar.shell.FileCompleter;
import org.hyperic.sigar.util.GetlineCompleter;

public class Iostat extends SigarCommandBase {

	public String[] ioResult = new String[] {};

    private static final String OUTPUT_FORMAT =
        "%-15s %-15s %10s %10s %7s %7s %5s %5s";

    private static final String[] HEADER = new String[] {
        "Filesystem",
        "Mounted on",
        "Reads",
        "Writes",
        "R-bytes",
        "W-bytes",
        "Queue",
        "Svctm",
    };

    private GetlineCompleter completer;

    public Iostat(Shell shell) {
        super(shell);
        setOutputFormat(OUTPUT_FORMAT);
        this.completer = new FileCompleter(shell);
    }

    public Iostat() {
        super();
        setOutputFormat(OUTPUT_FORMAT);
    }

    public GetlineCompleter getCompleter() {
        return this.completer;
    }

    protected boolean validateArgs(String[] args) {
        return args.length <= 1;
    }

    public String getSyntaxArgs() {
        return "[filesystem]";
    }

    public String getUsageShort() {
        return "Report filesystem disk i/o";
    }

    public void printHeader() {
        printf(HEADER);
    }

    private String svctm(double val) {
        return sprintf("%3.2f", new Object[] { Double.valueOf(val) });
    }

    public void output(String[] args) throws SigarException {
        if (args.length == 1) {
            String arg = args[0];
            if ((arg.indexOf('/') != -1) || (arg.indexOf('\\') != -1)) {
                outputFileSystem(arg);
            }
            else {
                outputDisk(arg);
            }
        }
        else {
            FileSystem[] fslist = this.proxy.getFileSystemList();
            printHeader();
            for (int i=0; i<fslist.length; i++) {
                if (fslist[i].getType() == FileSystem.TYPE_LOCAL_DISK) {
                    output(fslist[i]);
                }
            }
        }
    }

    public void outputFileSystem(String arg) throws SigarException {
        FileSystemMap mounts = this.proxy.getFileSystemMap();
        String name = FileCompleter.expand(arg);
        FileSystem fs = mounts.getMountPoint(name);

        if (fs != null) {
            printHeader();
            output(fs);
            return;
        }

        throw new SigarException(arg +
                                 " No such file or directory");
    }

    public void outputDisk(String name) throws SigarException {
        DiskUsage disk =
            this.sigar.getDiskUsage(name);

        ArrayList<String> items = new ArrayList<String>();
        printHeader();
        items.add(name);
        items.add("-");
        items.add(String.valueOf(disk.getReads()));
        items.add(String.valueOf(disk.getWrites()));

        if (disk.getReadBytes() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
            items.add("-");
        }
        else {
            items.add(Sigar.formatSize(disk.getReadBytes()));
            items.add(Sigar.formatSize(disk.getWriteBytes()));
        }

        if (disk.getQueue() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
        }
        else {
            items.add(svctm(disk.getQueue()));
        }

        if (disk.getServiceTime() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
        }
        else {
            items.add(svctm(disk.getServiceTime()));
        }

        printf(items);

        ioResult = (String[]) items.toArray();
    }

    public void output(FileSystem fs) throws SigarException {
        FileSystemUsage usage =
            this.sigar.getFileSystemUsage(fs.getDirName());

        ArrayList<String> items = new ArrayList<String>();

        items.add(fs.getDevName());
        items.add(fs.getDirName());
        items.add(String.valueOf(usage.getDiskReads()));
        items.add(String.valueOf(usage.getDiskWrites()));

        if (usage.getDiskReadBytes() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
            items.add("-");
        }
        else {
            items.add(Sigar.formatSize(usage.getDiskReadBytes()));
            items.add(Sigar.formatSize(usage.getDiskWriteBytes()));
        }

        if (usage.getDiskQueue() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
        }
        else {
            items.add(svctm(usage.getDiskQueue()));
        }
        if (usage.getDiskServiceTime() == Sigar.FIELD_NOTIMPL) {
            items.add("-");
        }
        else {
            items.add(svctm(usage.getDiskServiceTime()));
        }

        printf(items);
    }
}
