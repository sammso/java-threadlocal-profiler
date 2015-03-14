Java thread local profiler.

Little library for java application profiling. Collects all performance metrics under same umbrella, which can be printed out together. Best use with some logging framework. The core does not have any dependencies and because of that can be located on top of class loader hierarchy. Support for Spring-framework and Liferay.


Usage:

```

		Watch watch = ThreadLocalProfiler.start();
		
		ThreadLocalProfiler.stop(watch , getClass(),"doStuffMethod", "this might be slow");
```


Example log
```
             Timestamp:   Start: Elapsed:  ToNext: Action: THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.187        0      188       16 root THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.203       16      172       31 -com.sohlman.profiler.test.0.Doit.method0(..) THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.234       47        0       31 --com.sohlman.profiler.test.1.Doit.method0(..) THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.265       78        0       31 --com.sohlman.profiler.test.1.Doit.method1(..) THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.296      109        0       32 --com.sohlman.profiler.test.1.Doit.method2(..) THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.328      141        0       31 --com.sohlman.profiler.test.1.Doit.method3(..) THRESHOLD ROWINDENTIFIER
2010-09-22 20:36:12.359      172       16        0 --com.sohlman.profiler.test.1.Doit.method4(..) THRESHOLD ROWINDENTIFIER
```


