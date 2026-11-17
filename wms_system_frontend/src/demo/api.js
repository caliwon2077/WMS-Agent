const BASE = '/java-api/v1/demo';
export function token() { return sessionStorage.getItem('wms-demo-token') || ''; }
export async function api(path, options = {}) {
  const isForm = options.body instanceof FormData;
  const response = await fetch(BASE + path, {
    ...options,
    headers: { ...(isForm ? {} : { 'Content-Type': 'application/json' }), Authorization: `Bearer ${token()}`, ...options.headers },
    body: options.body == null || isForm ? options.body : JSON.stringify(options.body),
  });
  const result = await response.json().catch(() => ({ message: '服务暂不可用，请检查后端是否启动' }));
  if (!response.ok || !result.success) {
    if (response.status === 401 && !path.includes('/login')) window.dispatchEvent(new Event('demo-session-expired'));
    throw new Error(result.message || '请求失败');
  }
  return result.data;
}
export const post = (path, body = {}) => api(path, { method: 'POST', body });
export const query = values => '?' + new URLSearchParams(Object.entries(values).filter(([, v]) => v != null && v !== '')).toString();
export async function download(path, name) {
  const response = await fetch(BASE + path, { headers: { Authorization: `Bearer ${token()}` } });
  if (!response.ok) { const e = await response.json(); throw new Error(e.message || '下载失败'); }
  const url = URL.createObjectURL(await response.blob()); const a = document.createElement('a'); a.href = url; a.download = name; a.style.display = 'none'; document.body.appendChild(a); a.click(); setTimeout(() => { a.remove(); URL.revokeObjectURL(url); }, 5000);
}
export const stateName = s => ({ queued: '等待执行', running: '正在执行', success: '已完成', partial: '部分完成', needs_input: '待补资料', failed: '执行失败', cancelled: '已取消', interrupted: '重启中断', timeout: '已超时', waiting_clarification: '等待补充', confirmation_pending: '等待确认', pending: '待确认', committed: '已保存', preview: '可确认', invalid: '预检未通过', confirmed: '已确认', draft: '草稿' }[s] || s);
export const factoryName = id => id === 'ALL' ? '全部授权工厂' : `${Number(id?.replace('F', '')) || ''} 厂`;
export const prettyTime = t => t ? String(t).replace('T', ' ').slice(0, 19) : '—';
