package net.artresearch.api;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Component
public class HeapSizeLogger {

  private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

  @Scheduled(fixedRate = 60000)
  public void logHeapSize() {
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
    long usedHeapSize = heapMemoryUsage.getUsed() / (1024 * 1024);
    long maxHeapSize = heapMemoryUsage.getMax() / (1024 * 1024);
    long committedHeapSize = heapMemoryUsage.getCommitted() / (1024 * 1024);
    System.out.printf("Used Heap Size: %d MB%n", usedHeapSize);
    System.out.printf("Max Heap Size: %d MB%n", maxHeapSize);
    System.out.printf("Committed Heap Size: %d MB%n", committedHeapSize);
  }
}
