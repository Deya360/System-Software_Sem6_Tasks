#include <windows.h>
#include <string>
#include "mmsystem.h"

LRESULT CALLBACK WndProc (HWND, UINT, WPARAM, LPARAM) ;

static HWND hwnd, hwnd1, hwnd2;

int WINAPI WinMain (HINSTANCE hInstance, HINSTANCE hPrevInstance, PSTR szCmdLine, int iCmdShow) {

    WNDCLASSEX wndClassPreset ;
    wndClassPreset.cbSize        = sizeof (wndClassPreset) ;
    wndClassPreset.style         = CS_HREDRAW | CS_VREDRAW ;
    wndClassPreset.lpfnWndProc   = WndProc ;
    wndClassPreset.cbClsExtra    = 0 ;
    wndClassPreset.cbWndExtra    = 0 ;
    wndClassPreset.hInstance     = hInstance ;
    wndClassPreset.hIcon         = LoadIcon (nullptr, IDI_APPLICATION) ;
    wndClassPreset.hCursor       = LoadCursor (nullptr, IDC_ARROW) ;
    wndClassPreset.hIconSm       = LoadIcon (nullptr, IDI_APPLICATION) ;
    wndClassPreset.lpszMenuName  = nullptr ;


    WNDCLASSEX wndClassMW = wndClassPreset;

    std::wstring mwClassName = L"MainWindow";
    wndClassMW.hbrBackground = CreateSolidBrush(RGB(255,102,0));
    wndClassMW.lpszClassName = mwClassName.c_str() ;
    RegisterClassEx (&wndClassMW) ;

    hwnd = CreateWindow (
        mwClassName.c_str(),                    // window class name
        mwClassName.c_str(),                    // window caption
        WS_OVERLAPPEDWINDOW | WS_CLIPCHILDREN,  // window style
        200, 200,                               // initial x,y positions
        900, 500,                               // initial x,y sizes
        nullptr,                    // parent window handle
        nullptr,                    // window menu handle
        hInstance,               // program instance handle
        nullptr) ;                  // creation parameters


    WNDCLASSEX wndClassCh1 = wndClassPreset;
    std::wstring ch1ClassName = L"ChildWindow1";
    wndClassCh1.hbrBackground = CreateSolidBrush(RGB(204,255,204));
    wndClassCh1.lpszClassName = ch1ClassName.c_str() ;

    RegisterClassEx (&wndClassCh1) ;

    hwnd1 = CreateWindow (ch1ClassName.c_str(), ch1ClassName.c_str(), WS_CLIPSIBLINGS,
                          250, 300, 500, 320, hwnd, nullptr, hInstance, nullptr);


    WNDCLASSEX wndClassCh2 = wndClassPreset;
    std::wstring ch2ClassName = L"ChildWindow2";
    wndClassCh2.hbrBackground = CreateSolidBrush(RGB(0,0,255));
    wndClassCh2.lpszClassName = ch2ClassName.c_str() ;

    RegisterClassEx (&wndClassCh2) ;

    hwnd2 = CreateWindow (ch2ClassName.c_str(), ch2ClassName.c_str(), WS_CLIPSIBLINGS,
                          800, 250, 200, 130, hwnd, nullptr, hInstance, nullptr);


    ShowWindow (hwnd, iCmdShow);
    UpdateWindow (hwnd);

    MSG msg ;
    while (GetMessage (&msg, nullptr, 0, 0)) {
            TranslateMessage (&msg) ;
            DispatchMessage (&msg) ;
    }
    return msg.wParam;
}

LRESULT CALLBACK WndProc (HWND hwnd, UINT iMsg, WPARAM wParam, LPARAM lParam) {
    HDC hdc ;
    PAINTSTRUCT ps ;
    RECT rect ;

    switch (iMsg) {
        case WM_LBUTTONUP:
            ShowWindow(hwnd1, SW_SHOWNORMAL);
            return 0;

        case WM_RBUTTONUP:
            ShowWindow(hwnd2, SW_SHOWNORMAL);
            return 0;

        case WM_CREATE :
//            PlaySound (L"hellowin.wav", nullptr, SND_FILENAME | SND_ASYNC) ;
            return 0 ;

        case WM_PAINT :
            hdc = BeginPaint (hwnd, &ps) ;

            GetClientRect (hwnd, &rect) ;
//            DrawText(hdc, L"Hello, Windows!", -1, &rect, DT_SINGLELINE | DT_CENTER | DT_VCENTER) ;

            EndPaint (hwnd, &ps) ;
            return 0 ;

        case WM_DESTROY :
            PostQuitMessage (0) ;
            return 0 ;
        }

    return DefWindowProc (hwnd, iMsg, wParam, lParam) ;
}
