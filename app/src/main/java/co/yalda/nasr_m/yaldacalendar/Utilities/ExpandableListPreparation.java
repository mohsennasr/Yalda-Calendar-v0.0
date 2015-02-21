package co.yalda.nasr_m.yaldacalendar.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nasr_M on 2/16/2015.
 */
public class ExpandableListPreparation {

    private List<String> parentList;
    private HashMap<String, List<String>> childList;

    public ExpandableListPreparation() {
        prepareListData();
    }

    private void prepareListData() {
        parentList = new ArrayList<String>();
        childList = new HashMap<String, List<String>>();

        // Adding child data
        parentList.add("شرکت توزیع برق");
        parentList.add("بانک ملی ایران");
        parentList.add("به روز رسانی");

        // Adding child data
        List<String> ece = new ArrayList<String>();
        ece.add("مبلغ آخرین صورتحساب 250000 ریال");
        ece.add("مهلت پرداخت آخرین صورتحساب 1393/11/25");


        List<String> bankMeli = new ArrayList<String>();
        bankMeli.add("برداشت مبلغ 2564000 در تاریخ 139310/02");
        bankMeli.add("پرداخت قبض به شناسه 135465455 مبلغ 523000 در تاریخ 1393/10/05");
        bankMeli.add("واریز مبلغ 250000 در تاریخ 1393/10/05");
        bankMeli.add("مانده حساب مبلغ 256486200 ریال");

        List<String> update = new ArrayList<String>();
        update.add("دریافت آخرین تغییرات");
        update.add("به روز رسانی");

        childList.put(parentList.get(0), ece); // Header, Child data
        childList.put(parentList.get(1), bankMeli);
        childList.put(parentList.get(2), update);
    }

    public List<String> getParentList() {
        return parentList;
    }

    public HashMap<String, List<String>> getChildList() {
        return childList;
    }
}

