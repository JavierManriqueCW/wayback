//
//  WaybackWidget.swift
//  WaybackWidget
//
//  Created by Javier Manrique Pellejero on 24/11/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import WidgetKit
import SwiftUI

struct WaybackWidget: Widget {
    let kind: String = "WaybackWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                WaybackWidgetEntryView(entry: entry)
            } else {
                WaybackWidgetEntryView(entry: entry)
                    .background()
            }
        }
        .configurationDisplayName(localizedStringForKey("widget_title"))
        .description(localizedStringForKey("widget_description"))
        .supportedFamilies([.systemSmall])
        .contentMarginsDisabled()
    }
}

struct WaybackWidgetEntryView: View {
    let entry: SimpleEntry
    var body: some View {
        let backgroundImage = UIImage(named: "WidgetBackground")!
        let title = entry.isParked ? localizedStringForKey("widget_parked_title") : localizedStringForKey("widget_not_parked_title")
        let emojiImage = entry.isParked ? UIImage(named: "AsleepEmoji")! : UIImage(named: "SparklesEmoji")!
        
        VStack {
            Image(uiImage: emojiImage)
                .resizable()
                .foregroundColor(.white.opacity(0.6))
                .aspectRatio(contentMode: .fit)
                .frame(width: 15, height: 15)
                .padding(.top, 16)
            
            Text(title.uppercased())
                .font(Font.custom("Futura-Medium", size: 12))
                .foregroundColor(.white)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .widgetBackground {
            Image(uiImage: backgroundImage.resized(toWidth: 200)!)
                .resizable()
                .scaledToFill()
        }
    }
}

struct SimpleEntry: TimelineEntry {
    let isParked: Bool
    let date: Date
}
                            
struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> SimpleEntry {
        SimpleEntry(isParked: false, date: Date())
    }

    func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
        let entry = SimpleEntry(isParked: false, date: Date())
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
        let sharedDefaults = UserDefaults(suiteName: "group.com.jmp.wayback")
        let isParked = sharedDefaults?.integer(forKey: "is_parked") == 1
        let entry: SimpleEntry = SimpleEntry(isParked: isParked, date: Date())
        let timeline = Timeline(entries: [entry], policy: .never)
        completion(timeline)
    }
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
