import { LinkedQueue, LinkedNode } from "../datastruct/LinkedQueue";

class Handler extends LinkedNode {
    private evName: string;
    private callback: Function;

    constructor(evName: string, callback: Function) {
        super();
        this.evName = evName;
        this.callback = callback;
    }

    public getEventName(): string { return this.evName; }
    public getCallback(): Function { return this.callback; }
}

/**
 * 全局事件管理器，将绑定相同事件的处理器用链表存储，事件发生时轮询该链表
 */
class GlobalEventManager {

    private handlers: {[index: string]: LinkedQueue<Handler>};

    constructor() {
        this.handlers = {};
    }

    /**
     * 创建一个Handler，用于连接到document上
     * @param evName 
     */
    private createMainHandler(evName: string): Function {
        return (ev: any) => {
            let chain = this.handlers[evName];
            if (chain) {
                chain.forEach(
                    (handler) => handler.getCallback()(ev));
            }
        };
    }

    /**
     * 绑定事件
     * @param evName 
     * @param callback 
     * @returns
     * * Handler 用作解绑事件
     */
    public on(evName: string, callback: Function): Handler {
        let handler = new Handler(evName, callback);
        let chain = this.handlers[evName];
        if (!chain) {
            chain = this.handlers[evName] = new LinkedQueue<Handler>();
        }
        chain.enQueue(handler);
        if (!document[evName]) {
            document[evName] = this.createMainHandler(evName);
        }
        return handler;
    }

    /**
     * 解除事件绑定
     * @param handler
     * @returns 如果解绑成功返回true，失败返回false
     */
    public off(handler: Handler): boolean {
        const evName = handler.getEventName();
        if (!this.handlers[evName]) {
            return false;
        }
        try {
            this.handlers[evName].delete(handler.getIndex());
        }
        catch {
            return false;
        }
    }

}

export default new GlobalEventManager();