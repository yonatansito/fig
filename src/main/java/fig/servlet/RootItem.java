package fig.servlet;

import java.io.*;
import java.util.*;
import fig.basic.*;

/**
 * The root item.
 */
public class RootItem extends Item {
  public final BasketView basketView;
  public final FileView fileView;
  public final WorkerViewDB workerViewDB;
  public final DomainView domainView;

  public RootItem(String varDir) {
    super(null, "ROOT", null);
    IOUtils.createNewDirIfNotExistsEasy(varDir);
    addItem(this.basketView = new BasketView(this, "baskets", new File(varDir, "baskets").toString(), true));
    addItem(this.fileView = new FileView(this, "files", "", new FileFactory(), false, false));
    addItem(this.workerViewDB = new WorkerViewDB(this, "workers", new File(varDir, "workers").toString()));
    addItem(this.domainView = new DomainView(this, "domains", new File(varDir, "domains").toString()));
  }

  public FieldListMap getMetadataFields() {
    FieldListMap fields = new FieldListMap();
    fields.add("logUpdates", "Whether to log updates").mutable = true;
    fields.add("logWorkers", "Whether to log requests sent by workers").mutable = true;
    fields.add("verbose",    "Generally print out a lot of stuff").mutable = true;
    return fields;
  }

  protected Value getIntrinsicFieldValue(String fieldName) throws MyException { // OVERRIDE
    if(fieldName.equals("logUpdates")) return new Value(""+WebState.logUpdates);
    if(fieldName.equals("logWorkers")) return new Value(""+WebState.logWorkers);
    if(fieldName.equals("verbose"))    return new Value(""+WebState.verbose);
    return super.getIntrinsicFieldValue(fieldName);
  }
  protected void changeIntrinsicFieldValue(String fieldName, String value) throws MyException {
         if(fieldName.equals("logUpdates")) WebState.logUpdates = Boolean.parseBoolean(value);
    else if(fieldName.equals("logWorkers")) WebState.logWorkers = Boolean.parseBoolean(value);
    else if(fieldName.equals("verbose"))    WebState.verbose = Boolean.parseBoolean(value);
    else super.changeIntrinsicFieldValue(fieldName, value);
  }

  public void update(UpdateSpec spec, UpdateQueue.Priority priority) throws MyException { // OVERRIDE
    super.update(spec, priority);
    updateChildren(spec, priority);
  }

  protected boolean isView() { return true; }
  protected Item newItem(String name) throws MyException { throw MyExceptions.unsupported; }
}
