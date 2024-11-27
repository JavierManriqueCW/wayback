import UIKit
import SwiftUI
import WidgetKit
import SharedApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        KoinControllerKt.doInitKoin()
        initSharedFunctions()
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
    
    func initSharedFunctions() {
        let sharedDefaults = UserDefaults(suiteName: "group.com.jmp.wayback")
        IosFunctionsKt.updateWidget = { isParked in
            sharedDefaults?.set(isParked, forKey: "is_parked")
            sharedDefaults?.synchronize()
            WidgetCenter.shared.reloadTimelines(ofKind: "WaybackWidget")
        }
        
        IosFunctionsKt.deleteFile = { filePath in
            let fileManager = FileManager.default
            do {
                try fileManager.removeItem(atPath: filePath)
                print("File deleted successfully.")
            } catch {
                print("Failed to delete file: \(error.localizedDescription)")
            }
        }
    }
    
    func fileExists(at filePath: String) -> Bool {
        let fileManager = FileManager.default
        return fileManager.fileExists(atPath: filePath)
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.all)
    }
}
