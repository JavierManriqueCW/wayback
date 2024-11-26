import UIKit
import SwiftUI
import WidgetKit
import SharedApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let sharedDefaults = UserDefaults(suiteName: "group.com.jmp.wayback")
        KoinControllerKt.doInitKoin()
        IosFunctionsKt.updateWidget = { isParked in
            print("TSST: updateWidget = \(isParked)")
            sharedDefaults?.set(isParked, forKey: "is_parked")
            sharedDefaults?.synchronize()
            WidgetCenter.shared.reloadTimelines(ofKind: "WaybackWidget")
        }
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.all)
    }
}
