#include <iostream>
#include <windows.h>
#include "usb2lpt.h"

using namespace std;

HANDLE hAccess;

void outb(BYTE a, BYTE b) {
	BYTE IoData[2];
	DWORD BytesRet;
	IoData[0] = a;
	IoData[1] = b;
	DeviceIoControl(hAccess,IOCTL_VLPT_OutIn/*0x222010*/,IoData,sizeof(IoData),NULL,0,&BytesRet,NULL);
}

void sendTrigger(int label) { outb(0, label); }

BYTE inb(BYTE a) {
	BYTE IoData[1];
	DWORD BytesRet;
	IoData[0] = a|0x10;	// set the bit for read operations
	DeviceIoControl(hAccess,IOCTL_VLPT_OutIn/*0x222010*/,IoData,sizeof(IoData),IoData,sizeof(IoData),&BytesRet,NULL);
	return IoData[0];
}

void test_usb2lpt(BYTE x) {

 	for (int n=9; n; n--) {	// try backwards
  		TCHAR DevName[12];
  		wsprintf(DevName,"\\\\.\\LPT%u",n);
  		hAccess = CreateFile(DevName,GENERIC_READ|GENERIC_WRITE,0,NULL,OPEN_EXISTING,0,0);
  		if (hAccess!=INVALID_HANDLE_VALUE) goto found;
 	}
 	hAccess = 0;

found:
// Read the 8051 XRAM or ATmega flash address 6 where you get the firmware date as FAT date.
// If this fails, this is not a USB2LPT.
	WORD addr = 6;
	WORD date = 0;		// Date as FAT date
	DWORD BytesRet;
	if (DeviceIoControl(hAccess,IOCTL_VLPT_XramRead/*0x22228E*/,&addr,sizeof(addr),&date,sizeof(date),&BytesRet,NULL)) {
	    // this is a USB2LPT device of any version, and the firmware date can be made visible using
	    FILETIME ft;
	    DosDateTimeToFileTime(date,0,&ft);
	    SYSTEMTIME st;
	    FileTimeToSystemTime(&ft,&st);
	    TCHAR s[20];		// the string buffer where the date is put
	    GetDateFormat(LOCALE_USER_DEFAULT,0,&st,NULL,s,20);
	    // ...
	    //cout << "DeviceIoControl succeed!\n";
	    outb(0, x);
	    BYTE in_Data = inb(0);
	    cout << "parallelPort - LPT1 read label : " << int(in_Data) << "\n";
	}else{
	    // this is a standard parallel port or something else
	}

}

int main(int argc, char *argv[]) {
	if (argc == 1) {
		cout << "click run.bat to run the program.\n";
		return 0;
	}
	if(strlen(argv[1])==1){
	test_usb2lpt(argv[1][0]); // test_read_write [0 - 9]	
	}
	
	else{
		test_usb2lpt(argv[1][0]); 
		test_usb2lpt(argv[1][1]); 
	}
	
}
