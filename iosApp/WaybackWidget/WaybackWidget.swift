//
//  WaybackWidget.swift
//  WaybackWidget
//
//  Created by Javier Manrique Pellejero on 24/11/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI

struct WaybackWidgetEntryView : View {
    var body: some View {
        let image = UIImage(named: "WidgetBackground")!
        VStack{}.widgetBackground {
            Image(uiImage: image.resized(toWidth: 200)!)
                .resizable()
                .scaledToFill()
        }
    }
}

struct WaybackWidget: Widget {
    let kind: String = "WaybackWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: SimpleProvider()) { entry in
            if #available(iOS 17.0, *) {
                WaybackWidgetEntryView()
            } else {
                WaybackWidgetEntryView()
                    .background()
            }
        }
        .configurationDisplayName(localizedStringForKey("widget_title"))
        .description(localizedStringForKey("widget_description"))
        .supportedFamilies([.systemSmall])
        .contentMarginsDisabled()
    }
}
                            
                            
struct SimpleProvider: TimelineProvider {
    func placeholder(in context: Context) -> PlaceholderEntry {
        PlaceholderEntry()
    }

    func getSnapshot(in context: Context, completion: @escaping (PlaceholderEntry) -> ()) {
        completion(PlaceholderEntry())
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<PlaceholderEntry>) -> ()) {
        completion(Timeline(entries: [PlaceholderEntry()], policy: .never))
    }
}

struct PlaceholderEntry: TimelineEntry {
    let date = Date()
}

extension View {
    @ViewBuilder func widgetBackground<T: View>(@ViewBuilder content: () -> T) -> some View {
        if #available(iOS 17.0, *) {
            containerBackground(for: .widget, content: content)
        }else {
            background(content())
        }
    }
}

func localizedStringForKey(_ key: String, comment: String = "") -> String {
    return NSLocalizedString(key, tableName: nil, bundle: .main, comment: comment)
}

extension UIImage {
  func resized(toWidth width: CGFloat, isOpaque: Bool = true) -> UIImage? {
    let canvas = CGSize(width: width, height: CGFloat(ceil(width/size.width * size.height)))
    let format = imageRendererFormat
    format.opaque = isOpaque
    return UIGraphicsImageRenderer(size: canvas, format: format).image {
      _ in draw(in: CGRect(origin: .zero, size: canvas))
    }
  }
}
